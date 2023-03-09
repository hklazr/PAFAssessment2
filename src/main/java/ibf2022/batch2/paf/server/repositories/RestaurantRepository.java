package ibf2022.batch2.paf.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.utils.Utils;

import static ibf2022.batch2.paf.server.utils.Constants.*;

@Repository
public class RestaurantRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO: Task 2 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below

	// MONGO QUERY
	//db.restaurants.distinct("cuisine")

	public List<String> getCuisines() {
		Query query = Query.query(new Criteria());

		List<String> docs = mongoTemplate.findDistinct(query, FIELD_CUISINE, COLLECTION_RESTAURANTS, String.class);

		List<String> newDocs = docs.stream().map(cus -> {
						return cus.replace("/", "_");
					}).toList();

		return newDocs;
	}

	// TODO: Task 3 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	
	// MONGO QUERY
	// db.restaurants.find({'cuisine': 'African'}).sort({name: 1});

	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {

		cuisine = cuisine.replaceAll("_", "/");

		Criteria criteria = Criteria.where(FIELD_CUISINE).is(cuisine);
		Query query = Query.query(criteria)
					.with(Sort.by(Sort.Direction.ASC, FIELD_NAME));
		
		List<Document> docs = mongoTemplate.find(query, Document.class, COLLECTION_RESTAURANTS);
		List<Restaurant> restaurants = docs.stream()
										.map(doc -> Utils.toRestaurant(doc))
										.toList();

		return restaurants;
	}
	// TODO: Task 4 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	
	// MONGO QUERY

	// db.restaurants.find({'restaurant_id':'30075445'})

	// db.comments.find({'restaurant_id': '40356068'});
	
	public Optional<Restaurant> getRestaurantById(String id) {
		
		Criteria criteria = Criteria.where(FIELD_RESTAURANT_ID).is(id);
		Query query = Query.query(criteria);

		Document doc = mongoTemplate.findOne(query, Document.class, COLLECTION_RESTAURANTS);

		if (null == doc) {
			return Optional.empty();
		}

		//Comments check
		Restaurant restaurant = Utils.toRestaurant(doc);

		Criteria commentsCriteria = Criteria.where(FIELD_RESTAURANT_ID).is(id);
		Query commentsQuery = Query.query(commentsCriteria);

		List<Document> commentsDoc = mongoTemplate.find(commentsQuery, Document.class, COLLECTION_COMMENTS);
		
		if (null == commentsDoc) {
			restaurant.setComments(null);

		} else {
			restaurant.setComments(commentsDoc.stream()
									.map(d -> Utils.toComment(d))
									.toList());
		}

		return Optional.of(restaurant); 
	}

	// TODO: Task 5 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	
	/*  MONGO QUERY
		db.comments.insertOne({
		"restaurant_id" : "40552806",
		"name" : "Azhar",
		"rating" : 5,
		"comment" : "It's the BOMB!",
		"date" : 1678340304580
	 	})
	*/
	public void insertRestaurantComment(Comment comment) {

		Document d = Utils.toDocument(comment);
		mongoTemplate.insert(d, COLLECTION_COMMENTS);
		
		return;
	}
	
}