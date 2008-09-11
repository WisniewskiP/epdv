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
package org.mj.eclipse.reporting.classpath.mvc.models.internal;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.eclipse.core.resources.IProject;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.zest.layouts.constraints.BasicEntityConstraint;
import org.eclipse.zest.layouts.constraints.EntityPriorityConstraint;
import org.eclipse.zest.layouts.constraints.LabelLayoutConstraint;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;
import org.mj.eclipse.reporting.classpath.mvc.models.IConnector;
import org.mj.eclipse.reporting.classpath.mvc.models.INode;

/**
 * @author Mounir Jarraï
 * 
 */
public class ProjectModel extends AbstractModel implements INode, IPropertySource {

	private transient IProject project;
	private String name;

	private Collection<IConnector> outgoingConnections = new HashSet<IConnector>();
	private Collection<IConnector> incamingConnections = new HashSet<IConnector>();

	/**
	 * @param project
	 * @throws IllegalArgumentException
	 *             if <code>project</code> parameter is null
	 */
	protected ProjectModel(IProject project) {
		if (project == null) {
			throw new IllegalArgumentException("project parameter can't ne null");
		}
		this.project = project;
		this.name = this.project.getName().intern();
	}

	/**
	 * @return
	 * @see org.eclipse.core.resources.IResource#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the outgoingConnection
	 */
	public final Collection<IConnector> getOutgoingConnections() {
		synchronized (outgoingConnections) {
			return Collections.unmodifiableCollection(outgoingConnections);
		}
	}

	/**
	 * @return the incamingConnection
	 */
	public final Collection<IConnector> getIncomingConnections() {
		synchronized (incamingConnections) {
			return Collections.unmodifiableCollection(incamingConnections);
		}
	}

	private Collection<INode> getOutgoingProjects() {
		return CollectionUtils.collect(outgoingConnections, new Transformer<IConnector, INode>() {
			public INode transform(IConnector connector) {
				return connector.getTarget();
			}
		});
	}

	private Collection<INode> getIncomingProjects() {
		return CollectionUtils.collect(incamingConnections, new Transformer<IConnector, INode>() {
			public INode transform(IConnector connector) {
				return connector.getSource();
			}
		});
	}

	protected void addOutgoingConnection(IConnector connector) {
		synchronized (outgoingConnections) {
			outgoingConnections.add(connector);
		}
	}

	protected void addIncamingConnection(IConnector connector) {
		synchronized (incamingConnections) {
			incamingConnections.add(connector);
		}
	}

	/**
	 * @param other
	 * @return
	 * @see org.eclipse.core.resources.IResource#equals(java.lang.Object)
	 */
	public final boolean equals(Object other) {
		if (this == other)
			return true;

		if (other == null || !ProjectModel.class.equals(other.getClass()))
			return false;

		ProjectModel project = (ProjectModel) other;

		return name.equals(project.name);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/** *************************************************************************************** */
	// For Layout Management
	/** *************************************************************************************** */

	private Object internalNode;

	private double height = -1;
	private double width = -1;
	private double x = 0;
	private double y = 0;

	/**
	 * @see org.eclipse.mylyn.zest.layouts.LayoutEntity#getHeightInLayout()
	 */
	public double getHeightInLayout() {
		return height;
	}

	/**
	 * @see org.eclipse.zest.layouts.LayoutEntity#getWidthInLayout()
	 */
	public double getWidthInLayout() {
		return width;
	}

	/**
	 * @see org.eclipse.zest.layouts.LayoutEntity#getXInLayout()
	 */
	public double getXInLayout() {
		return x;
	}

	/**
	 * @see org.eclipse.zest.layouts.LayoutEntity#getYInLayout()
	 */
	public double getYInLayout() {
		return y;
	}

	/**
	 * @see org.eclipse.zest.layouts.LayoutEntity#setLocationInLayout(double, double)
	 */
	public void setLocationInLayout(double x, double y) {
		double oldX = this.x;
		this.x = x;
		double oldY = this.x;
		this.y = y;
		firePropertyChange(Properties.LOCATION.toString(), new Point(oldX, oldY), new Point(x, y));
	}

	/**
	 * @see org.eclipse.zest.layouts.LayoutEntity#setSizeInLayout(double, double)
	 */
	public void setSizeInLayout(double width, double height) {
		double oldWidth = this.width;
		this.width = width;
		double oldHeight = this.height;
		this.height = height;
		firePropertyChange(Properties.SIZE.toString(), new PrecisionDimension(oldWidth, oldHeight), new PrecisionDimension(width, height));
	}

	public void populateLayoutConstraint(LayoutConstraint constraint) {
		if (constraint instanceof LabelLayoutConstraint) {

		} else if (constraint instanceof BasicEntityConstraint) {
			//			BasicEntityConstraint entityConstraint = (BasicEntityConstraint) constraint;
			//			entityConstraint.hasPreferredSize = true;
			//			entityConstraint.preferredHeight = getHeightInLayout() * 1.618;
			//			entityConstraint.preferredWidth = getWidthInLayout() * 1.618;
		} else if (constraint instanceof EntityPriorityConstraint) {

		}
	}

	/**
	 * @see ca.uvic.cs.chisel.layouts.LayoutEntity#getInternalEntity()
	 */
	public Object getLayoutInformation() {
		return internalNode;
	}

	/**
	 * @see ca.uvic.cs.chisel.layouts.LayoutEntity#setInternalEntity(java.lang.Object)
	 */
	public void setLayoutInformation(Object internalEntity) {
		this.internalNode = internalEntity;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		if (obj == null || !ProjectModel.class.equals(obj.getClass())) {
			throw new IllegalArgumentException("obj parameter is null or has wrong type!");
		}
		ProjectModel project = (ProjectModel) obj;
		return getName().compareTo(project.getName());
	}

	/** *************************************************************************************** */
	// For Property viewer
	/** *************************************************************************************** */

	public static String PROJECT_NAME = "PROJECT_NAME"; //$NON-NLS-1$
	public static String TOP_DOWN_DEP = "TOP_DOWN_DEP"; //$NON-NLS-1$
	public static String BUTTOM_UP_DEP = "BUTTOM_UP_DEP"; //$NON-NLS-1$
	public static String POSITION = "POSITION"; //$NON-NLS-1$

	static IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] { 
		new PropertyDescriptor(PROJECT_NAME, "Project Name"),
		new PropertyDescriptor(TOP_DOWN_DEP, "Outgoing dependencies"), 
		new PropertyDescriptor(BUTTOM_UP_DEP, "Incoming dependencies"),
		new PropertyDescriptor(POSITION,"Position"),
	};

	final String POSITION_FORMAT = "({0, number, integer}, {1, number, integer})";
	
	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Object getPropertyValue(Object id) {
		if (PROJECT_NAME.equals(id))
			return getName();
		else if (TOP_DOWN_DEP.equals(id))
			return getOutgoingProjects();
		else if (BUTTOM_UP_DEP.equals(id))
			return getIncomingProjects();
		else if (POSITION.equals(id)) {
			return MessageFormat.format(POSITION_FORMAT, new Object[] { getXInLayout(), getYInLayout() });
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

}