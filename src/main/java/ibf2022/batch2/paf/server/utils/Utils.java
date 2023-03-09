package ibf2022.batch2.paf.server.utils;

import org.bson.Document;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import static ibf2022.batch2.paf.server.utils.Constants.*;

public class Utils {
    
    public static Restaurant toRestaurant(Document doc) {

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(doc.getString(FIELD_RESTAURANT_ID));
        restaurant.setName(doc.getString(FIELD_NAME));
        restaurant.setCuisine(doc.getString(FIELD_CUISINE));

        String address = "%s, %s, %s, %s".formatted(
            doc.get(FIELD_ADDRESS, Document.class).getString(FIELD_BUILDING),
            doc.get(FIELD_ADDRESS, Document.class).getString(FIELD_STREET),
            doc.get(FIELD_ADDRESS, Document.class).getString(FIELD_ZIPCODE),
            doc.getString(FIELD_BOROUGH));

        restaurant.setAddress(address);
        // restaurant.setComments(null);

        return restaurant;
    }

    public static JsonObject toJson(Restaurant restaurant) {

        JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("restaurantId", restaurant.getRestaurantId())
                        .add("name", restaurant.getName())
                        .add("cuisine", restaurant.getCuisine())
                        .add("address", restaurant.getAddress());

            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

            if (null != restaurant.getComments()){
                restaurant.getComments().forEach(c -> arrBuilder.add(Utils.toJson(c)));
            } 

            builder.add("comments", arrBuilder.build());
        
        return builder.build();
    }

    public static Comment toComment(Document doc) {
    
        Comment comment = new Comment();
        comment.setRestaurantId(doc.getString(FIELD_RESTAURANT_ID));
        comment.setName(doc.getString(FIELD_NAME));
        comment.setDate(doc.getLong(FIELD_DATE));
        comment.setComment(doc.getString(FIELD_COMMENT));
        comment.setRating(doc.getInteger(FIELD_RATING));

        return comment;
    }

    public static Document toDocument(Comment comment) {

        Document doc = new Document()
                        .append("restaurant_id", comment.getRestaurantId())
                        .append("name", comment.getName())
                        .append("rating", comment.getRating())
                        .append("comment", comment.getComment())
                        .append("date", comment.getDate());

        return doc;
    }

    public static JsonObject toJson(Comment comment) {

        return Json.createObjectBuilder()
                .add("date", comment.getDate())
                .add("restaurantId", comment.getRestaurantId())
                .add("name", comment.getName())
                .add("comment", comment.getComment())
                .add("rating", comment.getRating())
                .build();

    }

}