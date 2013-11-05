package ca.cmput301f13t03.adventure_datetime.model;

import io.searchbox.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import java.util.Collection;
import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class WebStorage {
	
	private JestClient client;
	private String errorMessage;
	
	public WebStorage() {
		client = ES.Client.getClient();
	}

	public Story getStory(UUID storyId) throws Exception {
		Get get = new Get.Builder("stories", storyId.toString()).build();
		JestResult result = execute(get);
		return result.getSourceAsObject(Story.class);
	}
	
	public Collection<Story> getAllStories() throws Exception {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(QueryBuilders.matchAllQuery());
		Search search = new Search.Builder(ssb.toString())
			.addIndex("stories")
			.addType("story")
			.build();
		
		JestResult result = execute(search);
		return result.getSourceAsObjectList(Story.class);
	}
	
	public StoryFragment getFragment(UUID fragmentId) throws Exception {
		Get get = new Get.Builder("fragments", fragmentId.toString()).build();
		JestResult result = execute(get);
		return result.getSourceAsObject(StoryFragment.class);
	}
	
	public Collection<StoryFragment> getAllFragmentsForStory(UUID storyId) throws Exception {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(QueryBuilders.matchQuery("storyId", storyId.toString()));
		Search search = new Search.Builder(ssb.toString())
			.addIndex("fragments")
			.addType("fragment")
			.build();
		
		JestResult result = execute(search);
		return result.getSourceAsObjectList(StoryFragment.class);
	}
	
	public Collection<Comment> getComments(UUID targetId) throws Exception {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(QueryBuilders.matchQuery("targetId", targetId.toString()));
		Search search = new Search.Builder(ssb.toString())
			.addIndex("comments")
			.addType("comment")
			.build();
		
		JestResult result = execute(search);
		return result.getSourceAsObjectList(Comment.class);
	}

	public boolean comment(Comment comment) throws Exception {
		Index index = new Index.Builder(comment).index("comments").type("comment").build();
		JestResult result = execute(index);
		return result.isSucceeded();
	}
	
	public boolean deleteComment(UUID commentId) throws Exception {
		JestResult result = execute(new Delete.Builder("comments", "comment", commentId.toString()).build());
		return result.isSucceeded();
	}
	
	// TODO: make this more clear on what part failed if it does fail
	public boolean publishStory(Story story, Collection<StoryFragment> fragments) throws Exception {
		Index index = new Index.Builder(story).index("stories").type("story").build();
		JestResult resultStory = execute(index);
		
		Bulk.Builder bulkBuilder = new Bulk.Builder()
			.defaultIndex("fragments")
			.defaultType("fragment");
		
		for (StoryFragment f : fragments) {
			bulkBuilder.addAction(new Index.Builder(f).build());
		}
		
		JestResult resultFragments = execute(bulkBuilder.build());
		return resultStory.isSucceeded() && resultFragments.isSucceeded();
	}

	public String getErrorMessagge() {
		return this.errorMessage;
	}
	
	private JestResult execute(Action clientRequest) throws Exception {
		JestResult result = client.execute(clientRequest);
		this.errorMessage = result.getErrorMessage();
		return result;
	}
}
