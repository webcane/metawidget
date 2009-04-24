// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.inspector.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for Inspectors that inspect Objects.
 * <p>
 * Handles iterating over an Object for properties and actions, and supporting pluggable property
 * and action conventions. Also handles unwrapping an Object wrapped by a proxy library (such as
 * CGLIB or Javassist).
 *
 * @author Richard Kennard
 */

public abstract class BaseObjectInspector
	implements Inspector
{
	//
	// Private statics
	//

	private final static Map<Class<? extends PropertyStyle>, PropertyStyle>	PROPERTY_STYLE_CACHE	= CollectionUtils.newHashMap();

	private final static Map<Class<? extends ActionStyle>, ActionStyle>		ACTION_STYLE_CACHE		= CollectionUtils.newHashMap();

	//
	// Private members
	//

	private PropertyStyle													mPropertyStyle;

	private ActionStyle														mActionStyle;

	//
	// Protected members
	//

	protected Log															mLog					= LogUtils.getLog( getClass() );

	//
	// Constructors
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All BaseObjectInspector-derived inspectors must be configurable, to allow configuring
	 * property styles and proxy patterns.
	 */

	protected BaseObjectInspector( BaseObjectInspectorConfig config )
	{
		try
		{
			// Property

			Class<? extends PropertyStyle> propertyStyle = config.getPropertyStyle();

			if ( propertyStyle != null )
			{
				mPropertyStyle = PROPERTY_STYLE_CACHE.get( propertyStyle );

				if ( mPropertyStyle == null )
				{
					mPropertyStyle = propertyStyle.newInstance();
					PROPERTY_STYLE_CACHE.put( propertyStyle, mPropertyStyle );
				}
			}

			// Action

			Class<? extends ActionStyle> actionStyle = config.getActionStyle();

			if ( actionStyle != null )
			{
				mActionStyle = ACTION_STYLE_CACHE.get( actionStyle );

				if ( mActionStyle == null )
				{
					mActionStyle = actionStyle.newInstance();
					ACTION_STYLE_CACHE.put( actionStyle, mActionStyle );
				}
			}
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		// If no type, return nothing

		if ( type == null )
			return null;

		try
		{
			Object childToInspect = null;
			String childName = null;
			Class<?> childDeclaredType;
			Map<String, String> parentAttributes = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful annotations...

				Object[] tuple = traverse( toInspect, type, true, names );

				if ( tuple == null )
					return null;

				childName = names[names.length - 1];
				Class<?> parentType = (Class<?>) tuple[1];

				Property propertyInParent = mPropertyStyle.getProperties( parentType ).get( childName );

				if ( propertyInParent == null )
					return null;

				childDeclaredType = propertyInParent.getType();

				// ...provided it has a getter

				if ( propertyInParent.isReadable() )
				{
					Object parentToInspect = tuple[0];
					childToInspect = propertyInParent.read( parentToInspect );
					parentAttributes = inspectProperty( propertyInParent, parentToInspect );
				}
			}

			// ...otherwise, just start at the end point

			else
			{
				Object[] tuple = traverse( toInspect, type, false );

				if ( tuple == null )
					return null;

				childToInspect = tuple[0];
				childDeclaredType = (Class<?>) tuple[1];
			}

			Document document = XmlUtils.newDocumentBuilder().newDocument();
			Element entity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			if ( childToInspect == null )
			{
				// If pointed directly at a type, return type information even
				// if the actual value is null. This is a special concession so
				// we can inspect parameterized types of Collections without having
				// to iterate over and grab the first element in that Collection

				if ( names == null || names.length == 0 )
					inspect( childToInspect, childDeclaredType, entity );
			}
			else
			{
				inspect( childToInspect, childToInspect.getClass(), entity );
			}

			// Nothing of consequence to return?

			if ( isInspectionEmpty( entity, parentAttributes ) )
				return null;

			// Start a new DOM Document

			Element root = document.createElementNS( NAMESPACE, ROOT );
			root.setAttribute( VERSION, "1.0" );
			document.appendChild( root );
			root.appendChild( entity );

			// Add any parent attributes

			if ( parentAttributes != null )
			{
				XmlUtils.setMapAsAttributes( entity, parentAttributes );
				entity.setAttribute( NAME, childName );
			}

			// Every Inspector needs to attach a type to the entity, so that CompositeInspector can
			// merge it. The type should be the *declared* type, not the *actual* type, as otherwise
			// subtypes (and proxied types) will stop XML and Object-based Inspectors merging back
			// together properly

			entity.setAttribute( TYPE, childDeclaredType.getName() );

			// Return the document

			return XmlUtils.documentToString( document, false );
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	/**
	 * @param toInspect
	 *            the object to inspect. May be null
	 * @param clazz
	 *            the class to inspect. If toInspect is not null, will be the actual class of
	 *            toInspect. If toInspect is null, will be the class to lookup
	 */

	protected void inspect( Object toInspect, Class<?> classToInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();

		// Inspect properties

		for ( Property property : getProperties( classToInspect ).values() )
		{
			Map<String, String> attributes = inspectProperty( property, toInspect );

			if ( attributes == null || attributes.isEmpty() )
				continue;

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, property.getName() );

			XmlUtils.setMapAsAttributes( element, attributes );

			toAddTo.appendChild( element );
		}

		// Inspect actions

		for ( Action action : getActions( classToInspect ).values() )
		{
			Map<String, String> attributes = inspectAction( action, toInspect );

			if ( attributes == null || attributes.isEmpty() )
				continue;

			Element element = document.createElementNS( NAMESPACE, ACTION );
			element.setAttribute( NAME, action.getName() );

			XmlUtils.setMapAsAttributes( element, attributes );

			toAddTo.appendChild( element );
		}
	}

	/**
	 * Inspect the given property and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 */

	protected abstract Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception;

	protected Map<String, Property> getProperties( Class<?> clazz )
	{
		if ( mPropertyStyle == null )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, Property> map = Collections.EMPTY_MAP;
			return map;
		}

		return mPropertyStyle.getProperties( clazz );
	}

	/**
	 * Inspect the given action and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 * <p>
	 * Note: unlike <code>inspectProperty</code>, this method has a default implementation that
	 * returns <code>null</code>. This is because most Inspectors will not implement
	 * <code>inspectAction</code>.
	 */

	protected Map<String, String> inspectAction( Action action, Object toInspect )
		throws Exception
	{
		return null;
	}

	protected Map<String, Action> getActions( Class<?> clazz )
	{
		if ( mActionStyle == null )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, Action> map = Collections.EMPTY_MAP;
			return map;
		}

		return mActionStyle.getActions( clazz );
	}

	/**
	 * Returns true if the inspection returned nothing of consequence. This is an optimization that
	 * allows our <code>Inspector</code> to return <code>null</code> overall, rather than
	 * creating and serializing an XML document, which <code>CompositeInspector</code> then
	 * deserializes and merges, all for no meaningful content.
	 *
	 * @return true if the inspection is 'empty'
	 */

	protected boolean isInspectionEmpty( Element elementEntity, Map<String, String> parentAttributes )
	{
		if ( elementEntity.hasChildNodes() )
			return false;

		if ( parentAttributes != null && !parentAttributes.isEmpty() )
			return false;

		return true;
	}

	//
	// Private methods
	//

	/**
	 * @return If found, a tuple of Object and declared type (not actual type). If not found,
	 *         returns null.
	 */

	private Object[] traverse( Object toTraverse, String type, boolean onlyToParent, String... names )
	{
		// Special support for class lookup

		if ( toTraverse == null )
		{
			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null )
				return null;

			return new Object[] { null, clazz };
		}

		// Use the toTraverse's ClassLoader, to support Groovy dynamic classes
		//
		// (note: for Groovy dynamic classes, this needs the applet to be signed - I think this is
		// still better than 'relaxing' this sanity check, as that would lead to differing behaviour
		// when deployed as an unsigned applet versus a signed applet)

		Class<?> clazz = ClassUtils.niceForName( type, toTraverse.getClass().getClassLoader() );

		if ( clazz == null || !clazz.isAssignableFrom( toTraverse.getClass() ) )
			return null;

		// Traverse through names (if any)

		Object traverse = toTraverse;
		Class<?> traverseDeclaredType = ClassUtils.niceForName( type );

		if ( names != null && names.length > 0 )
		{
			Object parentTraverse = null;
			Object parentTraverseDeclaredType = null;

			Set<Object> traversed = CollectionUtils.newHashSet();
			traversed.add( traverse );

			for ( String name : names )
			{
				Property property = mPropertyStyle.getProperties( traverse.getClass() ).get( name );

				if ( property == null || !property.isReadable() )
					return null;

				parentTraverse = traverse;
				parentTraverseDeclaredType = traverseDeclaredType;
				traverse = property.read( traverse );
				traverseDeclaredType = property.getType();

				if ( traverse == null )
					break;

				// Unlike BaseXmlInspector (which can never be certain it has detected a
				// cyclic reference because it only looks at types, not objects),
				// BaseObjectInspector can detect cycles and nip them in the bud

				if ( !traversed.add( traverse ) )
				{
					LogUtils.getLog( getClass() ).warn( ClassUtils.getSimpleName( getClass() ) + " prevented infinite recursion on " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + ". Consider annotating " + name + " as @UiHidden" );
					return null;
				}
			}

			if ( onlyToParent )
				return new Object[] { parentTraverse, parentTraverseDeclaredType };
		}

		return new Object[] { traverse, traverseDeclaredType };
	}
}
