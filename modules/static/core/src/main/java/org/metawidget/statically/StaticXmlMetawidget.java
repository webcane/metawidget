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

package org.metawidget.statically;

import java.util.Map;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public abstract class StaticXmlMetawidget
	extends StaticMetawidget
	implements StaticXmlWidget {

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	//
	// Public methods
	//

	public void putAttribute( String name, String value ) {

		if ( mAttributes == null ) {

			// (use TreeMap for consistent unit tests)

			mAttributes = CollectionUtils.newTreeMap();
		}

		mAttributes.put( name, value );
	}

	public String getAttribute( String name ) {

		if ( mAttributes == null ) {
			return null;
		}

		return mAttributes.get( name );
	}

	public String getTagPrefix() {

		return "m";
	}

	public String getTagNamespace() {

		// Metawidgets should never be output (kind of the point of being static)

		return null;
	}

	/**
	 * Recurse over all children and retrieve the namespaces they use.
	 *
	 * @return map of prefix and namespace
	 */

	public Map<String, String> getNamespaces() {

		Map<String, String> namespaces = CollectionUtils.newHashMap();
		populateNamespaces( this, namespaces );
		return namespaces;
	}

	//
	// Private methods
	//

	private void populateNamespaces( StaticXmlWidget xmlWidget, Map<String, String> namespaces ) {

		for ( StaticWidget child : xmlWidget.getChildren() ) {

			StaticXmlWidget xmlChild = (StaticXmlWidget) child;

			if ( xmlChild.getTagNamespace() != null ) {
				namespaces.put( xmlChild.getTagPrefix(), xmlChild.getTagNamespace() );
			}

			populateNamespaces( xmlChild, namespaces );
		}
	}
}
