// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.xsd;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspectionresultprocessor.xsd.XmlSchemaToJavaTypeMappingProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class XmlSchemaInspectorTest
	extends TestCase {

	public void testMissingFile() {

		try {
			new XmlSchemaInspector( new XmlSchemaInspectorConfig() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "No XML input file specified", e.getMessage() );
		}
	}

	public void testProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "shiporder", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "orderperson", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "shipto", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "item", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testDividedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder-divided.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "shiporder", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "orderperson", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "shipto", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "item", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testNamedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder-named.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "shiporder", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "orderperson", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "shipto", property.getAttribute( NAME ) );
		assertEquals( "shiptotype", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "item", property.getAttribute( NAME ) );
		assertEquals( "itemtype", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testNestedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder", "shipto" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "shipto", entity.getAttribute( NAME ) );
		assertEquals( "", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "city", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "country", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testDividedNestedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder-divided.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder", "shipto" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "shipto", entity.getAttribute( NAME ) );
		assertEquals( "shipto", entity.getAttribute( TYPE ) );
		assertEquals( entity.getAttributes().getLength(), 2 );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "city", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "country", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testQuirks() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/quirks.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "quirks" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "quirks", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "age", property.getAttribute( NAME ) );
		assertEquals( "xs:integer", property.getAttribute( TYPE ) );
		assertEquals( "0", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "100", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( property.getAttributes().getLength(), 4 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "dollars", property.getAttribute( NAME ) );
		assertEquals( "xs:integer", property.getAttribute( TYPE ) );
		assertEquals( "2", property.getAttribute( MAXIMUM_FRACTIONAL_DIGITS ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "password", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( "5", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( "8", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 4 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testInheritance() {

		// Supertype

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/personinfo.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "personinfo" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "personinfo", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "firstname", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "lastname", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );

		// Subtype

		inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/personinfo.xsd" ) ) );
		document = XmlUtils.documentFromString( inspector.inspect( null, "fullpersoninfo" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "fullpersoninfo", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "firstname", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "lastname", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "city", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "country", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testRealWorld1() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/acmt-007.xsd" ) ) );

		// Test normal

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "Authorisation1" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Authorisation1", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "MinAmtPerTx", property.getAttribute( NAME ) );
		assertEquals( "ActiveCurrencyAndAmount_SimpleType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "MaxAmtPerTx", property.getAttribute( NAME ) );
		assertEquals( "ActiveCurrencyAndAmount_SimpleType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "MaxAmtByPrd", property.getAttribute( NAME ) );
		assertEquals( "MaximumAmountByPeriod1", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( null, property.getNextSibling() );

		// Test xs:attribute

		document = XmlUtils.documentFromString( inspector.inspect( null, "ActiveCurrencyAndAmount" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "ActiveCurrencyAndAmount", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "Ccy", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( 1, entity.getChildNodes().getLength() );

		// Test xs:annotation

		document = XmlUtils.documentFromString( inspector.inspect( null, "AccountIdentification4Choice" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "AccountIdentification4Choice", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "IBAN", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "Othr", property.getAttribute( NAME ) );
		assertEquals( "GenericAccountIdentification1", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( 2, entity.getChildNodes().getLength() );
	}

	public void testRealWorld2() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/acmt-010.xsd" ) ) );

		// From top-level

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "AccountRequestAcknowledgementV01" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "AccountRequestAcknowledgementV01", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "Refs", property.getAttribute( NAME ) );
		assertEquals( "References5", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "AcctId", property.getAttribute( NAME ) );
		assertEquals( "AccountForAction1", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "OrgId", property.getAttribute( NAME ) );
		assertEquals( "OrganisationIdentification6", property.getAttribute( TYPE ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "AcctSvcrId", property.getAttribute( NAME ) );
		assertEquals( "BranchAndFinancialInstitutionIdentification4", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		// From path

		document = XmlUtils.documentFromString( inspector.inspect( null, "AccountRequestAcknowledgementV01", "Refs", "MsgId" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "MsgId", entity.getAttribute( NAME ) );
		assertEquals( "MessageIdentification1", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "Id", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( "1", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( "35", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 4 );

		// From non-existent

		assertEquals( null, XmlUtils.documentFromString( inspector.inspect( null, "AccountRequestAcknowledgementV02" ) ) );
		assertEquals( null, XmlUtils.documentFromString( inspector.inspect( null, "AccountRequestAcknowledgementV02", "Foo" ) ) );
	}

	public void testRealWorld3() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/trading-partner.xsd" ) ) );

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "PlasticCardInformationGroup_ComplexType", "AuthorizationResponseInformation" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "AuthorizationResponseGroup_ComplexType", entity.getAttribute( TYPE ) );
		assertEquals( entity.getChildNodes().getLength(), 0 );
	}

	public void testRealWorld4() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/ddms.xsd" ) ) );

		// Top level

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "MetacardInfoType" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "MetacardInfoType", entity.getAttribute( TYPE ) );
		assertEquals( entity.getAttributes().getLength(), 1 );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "creator", property.getAttribute( NAME ) );
		assertEquals( "ContactInfoType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "publisher", property.getAttribute( NAME ) );
		assertEquals( "ContactInfoType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contributor", property.getAttribute( NAME ) );
		assertEquals( "ContactInfoType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "pointOfContact", property.getAttribute( NAME ) );
		assertEquals( "ContactInfoType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 4 );

		// MetacardInfoType/creator

		document = XmlUtils.documentFromString( inspector.inspect( null, "MetacardInfoType", "creator" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "ContactInfoType", entity.getAttribute( TYPE ) );
		assertEquals( "creator", entity.getAttribute( NAME ) );
		assertEquals( entity.getAttributes().getLength(), 2 );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "organization", property.getAttribute( NAME ) );
		assertEquals( "OrganizationType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "person", property.getAttribute( NAME ) );
		assertEquals( "PersonType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "service", property.getAttribute( NAME ) );
		assertEquals( "ServiceType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "unknown", property.getAttribute( NAME ) );
		assertEquals( "UnknownType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 4 );

		// MetacardInfoType/creator/organization

		document = XmlUtils.documentFromString( inspector.inspect( null, "MetacardInfoType", "creator", "organization" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "OrganizationType", entity.getAttribute( TYPE ) );
		assertEquals( "organization", entity.getAttribute( NAME ) );
		assertEquals( entity.getAttributes().getLength(), 2 );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( "xs:token", property.getAttribute( TYPE ) );
		assertEquals( "1", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "phone", property.getAttribute( NAME ) );
		assertEquals( "xs:token", property.getAttribute( TYPE ) );
		assertEquals( "1", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "email", property.getAttribute( NAME ) );
		assertEquals( "xs:token", property.getAttribute( TYPE ) );
		assertEquals( "1", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "subOrganization", property.getAttribute( NAME ) );
		assertEquals( "simpleTokenType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 4 );

		// MetacardInfoType/creator/organization/subOrganization

		document = XmlUtils.documentFromString( inspector.inspect( null, "MetacardInfoType", "creator", "organization", "subOrganization" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "simpleTokenType", entity.getAttribute( TYPE ) );
		assertEquals( "subOrganization", entity.getAttribute( NAME ) );
		assertEquals( entity.getAttributes().getLength(), 2 );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "SecurityAttributesGroup", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( entity.getChildNodes().getLength(), 1 );
	}

	public void testSwingMetawidget() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/acmt-010.xsd" ) ) ) );
		metawidget.addInspectionResultProcessor( new XmlSchemaToJavaTypeMappingProcessor<SwingMetawidget>() );
		metawidget.setPath( "AccountRequestAcknowledgementV01" );

		assertEquals( "Refs:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		SwingMetawidget refsMetawidget = (SwingMetawidget) metawidget.getComponent( "Refs" );

		// Test xs:enumeration

		assertEquals( "Req Tp:", ( (JLabel) refsMetawidget.getComponent( 0 ) ).getText() );
		JComboBox reqTp = ( (JComboBox) refsMetawidget.getComponent( "ReqTp" ) );
		assertEquals( null, reqTp.getItemAt( 0 ) );
		assertEquals( "OPEN", reqTp.getItemAt( 1 ) );
		assertEquals( "MNTN", reqTp.getItemAt( 2 ) );
		assertEquals( "CLSG", reqTp.getItemAt( 3 ) );
		assertEquals( "VIEW", reqTp.getItemAt( 4 ) );

		// Test nesting

		assertEquals( "Msg Id:", ( (JLabel) refsMetawidget.getComponent( 2 ) ).getText() );
		SwingMetawidget msgIdMetawidget = (SwingMetawidget) refsMetawidget.getComponent( "MsgId" );

		assertEquals( "Id:", ( (JLabel) msgIdMetawidget.getComponent( 0 ) ).getText() );
		assertTrue( msgIdMetawidget.getComponent( "Id" ) instanceof JTextField );
		assertEquals( "Cre Dt Tm:", ( (JLabel) msgIdMetawidget.getComponent( 2 ) ).getText() );
		assertTrue( msgIdMetawidget.getComponent( "CreDtTm" ) instanceof JTextField );
		assertEquals( 5, msgIdMetawidget.getComponentCount() );

		assertEquals( "Acct Id:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		SwingMetawidget acctIdMetawidget = (SwingMetawidget) metawidget.getComponent( "AcctId" );

		assertEquals( "Id:", ( (JLabel) acctIdMetawidget.getComponent( 0 ) ).getText() );
		SwingMetawidget idMetawidget = (SwingMetawidget) acctIdMetawidget.getComponent( "Id" );

		assertEquals( "IBAN:", ( (JLabel) idMetawidget.getComponent( 0 ) ).getText() );
		assertTrue( idMetawidget.getComponent( "IBAN" ) instanceof JTextField );

		// Test xs:any

		SwingMetawidget dgtlSgntrMetawidget = (SwingMetawidget) metawidget.getComponent( "DgtlSgntr" );
		SwingMetawidget sgntrMetawidget = (SwingMetawidget) dgtlSgntrMetawidget.getComponent( "Sgntr" );
		assertEquals( 0, sgntrMetawidget.getComponentCount() );
	}

	public void testBankAccount() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/bank-account.xsd" ) ) );

		// From top-level

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "accountSummary" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "accountSummary", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "timestamp", property.getAttribute( NAME ) );
		assertEquals( "xsd:dateTime", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "currency", property.getAttribute( NAME ) );
		assertEquals( "xsd:string", property.getAttribute( TYPE ) );
		assertEquals( "AUD,BRL,CAD,CNY,EUR,GBP,INR,JPY,RUR,USD", property.getAttribute( LOOKUP ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "balance", property.getAttribute( NAME ) );
		assertEquals( "xsd:decimal", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "interest", property.getAttribute( NAME ) );
		assertEquals( "xsd:decimal", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );

		// From path

		document = XmlUtils.documentFromString( inspector.inspect( null, "accountSummary", "timestamp" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "timestamp", entity.getAttribute( NAME ) );
		assertEquals( "xsd:dateTime", entity.getAttribute( TYPE ) );
		assertEquals( entity.getAttributes().getLength(), 2 );

		assertTrue( !entity.hasChildNodes() );

		document = XmlUtils.documentFromString( inspector.inspect( null, "accountSummary", "currency" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "currency", entity.getAttribute( NAME ) );
		assertEquals( "iso3currency", entity.getAttribute( TYPE ) );
		assertEquals( "AUD,BRL,CAD,CNY,EUR,GBP,INR,JPY,RUR,USD", entity.getAttribute( LOOKUP ) );
		assertEquals( entity.getAttributes().getLength(), 3 );

		assertTrue( !entity.hasChildNodes() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( XmlSchemaInspectorConfig.class, new XmlSchemaInspectorConfig() {
			// Subclass
		}, "inputStreams" );
	}

	public static void main( String[] args ) {

		final SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new XmlSchemaInspector(
				new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/ddms.xsd" ) ) ) );
		metawidget.addInspectionResultProcessor( new XmlSchemaToJavaTypeMappingProcessor<SwingMetawidget>() );
		metawidget.setPath( "MetacardInfoType" );

		JFrame frame = new JFrame( "Metawidget WSDL" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( new JScrollPane( metawidget ) );
		frame.setSize( 400, 210 );
		metawidget.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		frame.setVisible( true );
	}
}
