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

package org.metawidget.inspector.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.struts.StrutsInspector;
import org.metawidget.inspector.struts.StrutsInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class StrutsInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testInspection()
	{
		StrutsInspectorConfig config = new StrutsInspectorConfig();
		ConfigReader configReader = new ConfigReader();
		config.setInputStreams( configReader.openResource( "org/metawidget/inspector/struts/test-struts-config1.xml" ), configReader.openResource( "org/metawidget/inspector/struts/test-struts-config2.xml" ));
		StrutsInspector inspector = new StrutsInspector( config );

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "testForm1" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( "testForm1".equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "foo".equals( property.getAttribute( NAME ) ) );
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "bar".equals( property.getAttribute( NAME ) ) );
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "baz".equals( property.getAttribute( NAME ) ) );
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "abc".equals( property.getAttribute( NAME ) ) );
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );
	}
}