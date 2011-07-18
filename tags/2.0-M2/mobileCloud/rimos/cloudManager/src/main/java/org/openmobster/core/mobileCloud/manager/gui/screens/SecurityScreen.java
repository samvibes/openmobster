/**
 * Copyright (c) {2003,2009} {openmobster@gmail.com} {individual contributors as indicated by the @authors tag}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openmobster.core.mobileCloud.manager.gui.screens;

import java.util.Vector;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.system.Display;

import org.openmobster.core.mobileCloud.manager.gui.LocaleKeys;
import org.openmobster.core.mobileCloud.rimos.configuration.Configuration;
import org.openmobster.core.mobileCloud.api.ui.framework.Services;
import org.openmobster.core.mobileCloud.api.ui.framework.SystemLocaleKeys;
import org.openmobster.core.mobileCloud.api.ui.framework.navigation.Screen;
import org.openmobster.core.mobileCloud.api.ui.framework.resources.AppResources;
import org.openmobster.core.mobileCloud.api.ui.framework.command.CommandContext;



/**
 * @author openmobster@gmail.com
 */
public class SecurityScreen extends Screen
{		
	private MainScreen screen;
	private ListField listField;
	
	public SecurityScreen()
	{
		
	}
	//-------------------------------------------------------------------------------------------------------------------------------------------------
	public Object getContentPane() 
	{		
		return this.screen;
	}
	
	public void render()
	{								
		AppResources appResources = Services.getInstance().getResources();			
		this.screen = new MainScreen();
		this.screen.setTitle(appResources.localize(LocaleKeys.security, LocaleKeys.security));
		
		listField = new ListField(1);
		listField.setCallback(new ListFieldCallbackImpl());
		
		this.screen.add(listField);
		this.setMenuItems();
	}
	
	private void setMenuItems()
	{		
		AppResources resources = Services.getInstance().getResources();
		
		MenuItem selectItem = new MenuItem(resources.localize(LocaleKeys.select, LocaleKeys.select), 1, 1){
			public void run()
			{
				//UserInteraction/Event Processing...this is where the Commands can be executed
				SecurityScreen.this.handle();
			}
		};
		this.screen.addMenuItem(selectItem);
		
		MenuItem backItem = new MenuItem(resources.localize(SystemLocaleKeys.back, SystemLocaleKeys.back), 2, 2){
			public void run()
			{
				//Go Home
				Services.getInstance().getNavigationContext().home();
			}
		};								 												
		this.screen.addMenuItem(backItem);
	}	
	
	private void handle()
	{				
		CommandContext commandContext = new CommandContext();
		commandContext.setTarget("switchSSLMode");		
		Services.getInstance().getCommandService().execute(commandContext);
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	private static class ListFieldCallbackImpl implements ListFieldCallback
	{
		private Vector options;
		
		private ListFieldCallbackImpl()
		{
			this.options = new Vector();
			Configuration configuration = Configuration.getInstance();
			AppResources resources = Services.getInstance().getResources();
			if(configuration.isSSLActivated())
			{
				this.options.addElement(resources.localize(LocaleKeys.deactivate_ssl, LocaleKeys.deactivate_ssl));
			}
			else
			{
				this.options.addElement(resources.localize(LocaleKeys.activate_ssl, LocaleKeys.activate_ssl));
			}
		}

		public void drawListRow(ListField listField, Graphics graphics, int index,
		int y, int width) 
		{
			String action = (String)this.options.elementAt(index);									
			graphics.drawText(action, 0, y);
		}
		
		public Object get(ListField listField, int index) 
		{			
			return (String)this.options.elementAt(index);
		}
		
		public int indexOfList(ListField listField, String prefix, int start) 
		{			
			return this.options.indexOf(prefix, start);
		}

		public int getPreferredWidth(ListField listField) 
		{			
			return Display.getWidth();
		}				
	}			
}