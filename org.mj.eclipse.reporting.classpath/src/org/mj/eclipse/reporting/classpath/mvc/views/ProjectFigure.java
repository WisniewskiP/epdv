/**
 * Copyright (c) 2008, Mounir Jarraï
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    3. All advertising materials mentioning features or use of this software
 *       must display the following acknowledgement:
 *			This product includes software developed by Mounir Jarraï
 *      	and its contributors.
 *    4. Neither the name Mounir Jarraï nor the names of its contributors may 
 *       be used to endorse or promote products derived from this software 
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY MOUNIR JARRAÏ ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MOUNIR JARRAÏ BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package org.mj.eclipse.reporting.classpath.mvc.views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.mj.eclipse.reporting.classpath.Activator;
import org.mj.eclipse.reporting.classpath.preferences.PreferenceConstants;

/**
 * @author Mounir Jarraï
 * 
 */
public final class ProjectFigure extends Figure {

	private static final Insets INSETS = new Insets(0, 20, 0, 20);

	private static final Font steriotypeFont = new Font(null, "Arial", 14, SWT.ITALIC);

	private Label projectNameLabel;

	private TitleBarBorder titleBarBorder;

	private Color titleColor = ColorConstants.lightGray;

	public ProjectFigure() {
		ToolbarLayout toolbarLayout = new ToolbarLayout();
		toolbarLayout.setSpacing(10);
		toolbarLayout.setStretchMinorAxis(false);
		setLayoutManager(toolbarLayout);
		setOpaque(true);

		titleBarBorder = new TitleBarBorder("<< Project >>");
		titleBarBorder.setFont(steriotypeFont);

		titleBarBorder.setTextColor(ColorConstants.darkBlue);
		titleBarBorder.setBackgroundColor(titleColor);

		titleBarBorder.setPadding(INSETS);
		titleBarBorder.setTextAlignment(PositionConstants.CENTER);

		LineBorder lineBorder = new LineBorder(ColorConstants.black, 1);
		setBorder(new CompoundBorder(lineBorder, titleBarBorder));

		projectNameLabel = new Label();
		projectNameLabel.setOpaque(false);
		try {
			projectNameLabel.setFont(new Font(null, new FontData(Activator.getDefault().getPluginPreferences().getString(
					PreferenceConstants.PROJECT_NAME_FONT))));
		} catch (Throwable e) {
			projectNameLabel.setFont(new Font(null, "Arial", 12, SWT.NORMAL));
		}
		projectNameLabel.setLabelAlignment(PositionConstants.ALWAYS_LEFT);
		projectNameLabel.setLabelAlignment(PositionConstants.LEFT);
		projectNameLabel.setBackgroundColor(ColorConstants.white);
		projectNameLabel.setForegroundColor(ColorConstants.black);

		add(projectNameLabel);
	}

	/**
	 * @see org.mj.eclipse.reporting.classpath.mvc.views.IProjectFigure#setName(java.lang.String)
	 */
	public void setName(String name) {
		projectNameLabel.setText(name);
	}

	/**
	 * @param titleColor
	 */
	public void setTitleColor(Color titleColor) {
		this.titleColor = titleColor;
		titleBarBorder.setBackgroundColor(this.titleColor);
		revalidate();
		repaint();
	}

	/**
	 * @see org.mj.eclipse.reporting.classpath.mvc.views.IProjectFigure#getContentPane()
	 */
	public IFigure getContentPane() {
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return projectNameLabel.getText();
	}

}
