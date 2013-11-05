package ca.cmput301f13t03.adventure_datetime.serviceLocator;

import android.content.Context;
import ca.cmput301f13t03.adventure_datetime.controller.UserController;
import ca.cmput301f13t03.adventure_datetime.model.StoryManager;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IReaderStorage;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryModelDirector;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryModelPresenter;

public final class Locator 
{
	private static IStoryModelDirector s_director = null;
	private static IStoryModelPresenter s_presenter = null;
	private static UserController s_userController = null;
	//private static AuthorController s_authorController = null;
	
	public static void initializeLocator(Context applicationContext)
	{
		StoryManager manager = new StoryManager(applicationContext);
		IReaderStorage localDatabase = null; // todo::jt fill this in
		
		s_director = manager;
		s_presenter = manager;
		s_userController = new UserController(s_director, localDatabase);
	}
	
	public static IStoryModelDirector getDirector()
	{
		if(s_director != null)
		{
			return s_director;
		}
		else
		{
			throw new RuntimeException("You fucked up and forgot to initialize the locator!");
		}
	}
	
	public static IStoryModelPresenter getPresenter()
	{
		if(s_presenter != null)
		{
			return s_presenter;
		}
		else
		{
			throw new RuntimeException("You fucked up and forgot to initialize the locator!");
		}
	}
	
	public static UserController getUserController()
	{
		if(s_presenter != null)
		{
			return s_userController;
		}
		else
		{
			throw new RuntimeException("You fucked up and forgot to initialize the locator!");
		}
	}
}
