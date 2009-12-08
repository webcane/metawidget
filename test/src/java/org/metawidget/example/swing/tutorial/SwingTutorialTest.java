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

package org.metawidget.example.swing.tutorial;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.java5.Java5Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayoutConfig;
import org.metawidget.swing.layout.SeparatorSectionLayoutDecorator;
import org.metawidget.swing.layout.SeparatorSectionLayoutDecoratorConfig;

/**
 * @author Richard Kennard
 */

public class SwingTutorialTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testTutorial()
		throws Exception
	{
		// Check start of tutorial

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new Person() );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( metawidget.getComponent( 6 ) instanceof JPanel );
		assertTrue( 7 == metawidget.getComponentCount() );

		// Check middle of tutorial

		CompositeInspectorConfig inspectorConfig = new CompositeInspectorConfig().setInspectors(
				new PropertyTypeInspector(),
				new MetawidgetAnnotationInspector(),
				new Java5Inspector() );
		metawidget.setInspector( new CompositeInspector( inspectorConfig ) );
		GridBagLayoutConfig nestedLayoutConfig = new GridBagLayoutConfig().setNumberOfColumns( 2 );
		SeparatorSectionLayoutDecoratorConfig layoutConfig = new SeparatorSectionLayoutDecoratorConfig().setLayout(
		new org.metawidget.swing.layout.GridBagLayout( nestedLayoutConfig ));
		metawidget.setMetawidgetLayout( new SeparatorSectionLayoutDecorator( layoutConfig ));
		metawidget.setToInspect( new PersonAtTutorialEnd() );

		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 2 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 4 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( "Gender:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 6 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 7 ) instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 7 ) ).getModel().getSize() );
		assertTrue( "Notes:".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 8 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 9 ) instanceof JScrollPane );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 9 ) ) ).weighty );

		JPanel separatorPanel = (JPanel) metawidget.getComponent( 10 );
		assertTrue( "Work".equals( ((JLabel) separatorPanel.getComponent( 0 )).getText() ) );
		assertTrue( separatorPanel.getComponent( 1 ) instanceof JSeparator );

		assertTrue( "Employer:".equals( ( (JLabel) metawidget.getComponent( 11 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 12 ) instanceof JTextField );
		assertTrue( "Department:".equals( ( (JLabel) metawidget.getComponent( 13 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 14 ) instanceof JTextField );

		assertTrue( 15 == metawidget.getComponentCount() );

		// Check end of tutorial

		Stub stub = new Stub();
		stub.setName( "retired" );
		metawidget.add( stub );
		metawidget.setConfig( "org/metawidget/example/swing/tutorial/metawidget.xml" );
		metawidget.setMetawidgetLayout( new SeparatorSectionLayoutDecorator( new SeparatorSectionLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) )));

		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 2 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( "Gender:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 4 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 5 ) instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 5 ) ).getModel().getSize() );
		assertTrue( "Notes:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 6 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 7 ) ) ).weighty );

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 8 );
		assertTrue( "Work".equals( tabbedPane.getTitleAt( 0 ) ));

		JPanel panel = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( "Employer:".equals( ( (JLabel) panel.getComponent( 0 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( ( panel.getComponent( 0 ) ) ).gridx );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Department:".equals( ( (JLabel) panel.getComponent( 2 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) panel.getLayout() ).getConstraints( ( panel.getComponent( 2 ) ) ).gridx );
		assertTrue( panel.getComponent( 3 ) instanceof JTextField );

		assertTrue( 9 == metawidget.getComponentCount() );
	}

	public void testSectionAtEnd()
		throws Exception
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new PersonWithSectionAtEnd() );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );

		JPanel separatorPanel = (JPanel) metawidget.getComponent( 4 );
		assertTrue( "foo".equals( ((JLabel) separatorPanel.getComponent( 0 )).getText() ) );
		assertTrue( separatorPanel.getComponent( 1 ) instanceof JSeparator );

		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 5 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 6 ) instanceof JCheckBox );
		assertTrue( metawidget.getComponent( 7 ) instanceof JPanel );
		assertTrue( 8 == metawidget.getComponentCount() );
	}

	/**
	 * Check JFrame.addNotify bug (only see this if the JFrame actually tries to display)
	 */

	public void testAddNotify()
	{
		// Data model

		Person person = new Person();

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( person );
		Stub stub = new Stub();
		stub.setName( "retired" );
		metawidget.add( stub );

		// JFrame

		JFrame frame = new JFrame( "Metawidget Tutorial" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		frame.addNotify();
	}

	//
	// Inner class
	//

	static class PersonWithSectionAtEnd
	{
		public String	name;

		public int		age;

		@UiSection( "foo" )
		public boolean	retired;
	}

	public static class PersonAtTutorialEnd
	{
		public String	name;

		@UiComesAfter( "name" )
		public int		age;

		@UiComesAfter( "age" )
		public boolean	retired;

		@UiComesAfter( "retired" )
		public Gender	gender;

		public enum Gender
		{
			Male, Female
		}

		@UiComesAfter( "gender" )
		@UiLarge
		public String	notes;

		@UiComesAfter( "notes" )
		@UiSection( "Work" )
		public String	employer;

		@UiComesAfter( "employer" )
		public String	department;
	}
}
