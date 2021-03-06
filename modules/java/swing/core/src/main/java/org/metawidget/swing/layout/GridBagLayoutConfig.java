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

package org.metawidget.swing.layout;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Configures a GridBagLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GridBagLayoutConfig {

	//
	// Private members
	//

	private int		mNumberOfColumns	= 1;

	private int		mLabelAlignment		= SwingConstants.LEFT;

	private Color	mLabelForeground;

	private Font	mLabelFont;

	private boolean	mSupportMnemonics	= true;

	private String	mLabelSuffix		= StringUtils.SEPARATOR_COLON;

	private int		mRequiredAlignment	= SwingConstants.CENTER;

	private String	mRequiredText		= "*";

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setNumberOfColumns( int numberOfColumns ) {

		if ( numberOfColumns < 0 ) {
			throw LayoutException.newException( "numberOfColumns must be >= 0" );
		}

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelAlignment( int labelAlignment ) {

		mLabelAlignment = labelAlignment;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelForeground( Color labelForeground ) {

		mLabelForeground = labelForeground;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelFont( Font labelFont ) {

		mLabelFont = labelFont;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setSupportMnemonics( boolean supportMnemonics ) {

		mSupportMnemonics = supportMnemonics;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelSuffix( String labelSuffix ) {

		mLabelSuffix = labelSuffix;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setRequiredAlignment( int requiredAlignment ) {

		mRequiredAlignment = requiredAlignment;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setRequiredText( String requiredText ) {

		mRequiredText = requiredText;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		if ( mNumberOfColumns != ( (GridBagLayoutConfig) that ).mNumberOfColumns ) {
			return false;
		}

		if ( mLabelAlignment != ( (GridBagLayoutConfig) that ).mLabelAlignment ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelFont, ( (GridBagLayoutConfig) that ).mLabelFont ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelForeground, ( (GridBagLayoutConfig) that ).mLabelForeground ) ) {
			return false;
		}

		if ( mSupportMnemonics != ( (GridBagLayoutConfig) that ).mSupportMnemonics ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelSuffix, ( (GridBagLayoutConfig) that ).mLabelSuffix ) ) {
			return false;
		}

		if ( mRequiredAlignment != ( (GridBagLayoutConfig) that ).mRequiredAlignment ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mRequiredText, ( (GridBagLayoutConfig) that ).mRequiredText ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mNumberOfColumns;
		hashCode = 31 * hashCode + mLabelAlignment;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelFont );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelForeground );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSupportMnemonics );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelSuffix );
		hashCode = 31 * hashCode + mRequiredAlignment;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRequiredText );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	protected int getLabelAlignment() {

		return mLabelAlignment;
	}

	protected Color getLabelForeground() {

		return mLabelForeground;
	}

	protected Font getLabelFont() {

		return mLabelFont;
	}

	protected boolean isSupportMnemonics() {

		return mSupportMnemonics;
	}

	protected String getLabelSuffix() {

		return mLabelSuffix;
	}

	protected int getRequiredAlignment() {

		return mRequiredAlignment;
	}

	protected String getRequiredText() {

		return mRequiredText;
	}
}
