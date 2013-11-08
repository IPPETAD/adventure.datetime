package ca.cmput301f13t03.adventure_datetime.serviceLocator;

import android.content.Context;
import ca.cmput301f13t03.adventure_datetime.controller.AuthorController;
import ca.cmput301f13t03.adventure_datetime.controller.UserController;
import ca.cmput301f13t03.adventure_datetime.model.StoryManager;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IReaderStorage;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryModelDirector;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryModelPresenter;

/**
 * Locator class used to tie the view, model and controller componenets together into a cohesive whole.
 * @author Jesse
 */
public final class Locator 
{
	private static IStoryModelDirector s_director = null;
	private static IStoryModelPresenter s_presenter = null;
	private static UserController s_userController = null;
	private static AuthorController s_authorController = null;
	
	/**
	 * Setup the applications model and controllers
	 * MUST be called before the application attempts to access
	 * any controllers or model info
	 * @param applicationContext
	 */
	public static void initializeLocator(Context applicationContext)
	{
		StoryManager manager = new StoryManager(applicationContext);
		IReaderStorage localDatabase = null; // todo::jt fill this in
		
		s_director = manager;
		s_presenter = manager;
		s_userController = new UserController(s_director, localDatabase);
		s_authorController = new AuthorController(s_director);
	}
	
	/**
	 * Get the model's presenter. Used for fetching data from the model
	 */
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
	
	/**
	 * Get the user controller for the application.
	 * Used for viewing and browsing stories
	 */
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
	public static AuthorController getAuthorController() {
		if (s_presenter != null)
			return s_authorController;
		else
			throw new RuntimeException("You fucked up and forgot to initialize the locator!");
	}
}
