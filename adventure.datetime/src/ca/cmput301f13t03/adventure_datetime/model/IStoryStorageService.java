package ca.cmput301f13t03.adventure_datetime.model;

import java.util.Collection;

public interface IStoryStorageService {
	
	Story getStory(long id);
	
	Collection<Story> getAllStories();
	
	StoryFragment getFragment(long storyId, long fragmentId);
	
	Collection<StoryFragment> getAllFragmentsForStory(long storyId); 
	
	int publishStory(Story story, Collection<StoryFragment> fragments);
}
