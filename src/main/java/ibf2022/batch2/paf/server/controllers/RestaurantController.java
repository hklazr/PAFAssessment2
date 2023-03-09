package ibf2022.batch2.paf.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.services.RestaurantService;
import ibf2022.batch2.paf.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@RestController
@RequestMapping("/api")
public class RestaurantController {

	@Autowired
	RestaurantService restaurantService;

	// TODO: Task 2 - request handler

	// Gets all cuisines
	@GetMapping(path="/cuisines", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> getCuisines() {
		
			List<String> cuisines = restaurantService.getCuisines();

			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			cuisines.forEach(c -> arrBuilder.add(c));
		
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(arrBuilder.build().toString());
	}

	// TODO: Task 3 - request handler

	// Gets all the restaurants by cuisine
    @GetMapping(path="/restaurants/{cuisine}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<Restaurant>> getRestaurantsByCuisines(@PathVariable String cuisine) {
			
			List<Restaurant> rest = restaurantService.getRestaurantsByCuisine(cuisine);
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(rest);
    }


	// TODO: Task 4 - request handler

	// Individual restaurant after choosing a restaurant
    @GetMapping(path="restaurant/{restaurant_id}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> getRestaurantById(@PathVariable(name="restaurant_id") String id) {
			
			Optional<Restaurant> rest = restaurantService.getRestaurantById(id);
			
			if (rest.isEmpty()){
				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.body(Json.createObjectBuilder()
								.add("error", "%s is missing".formatted(id))
								.build().toString());
			}

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(Utils.toJson(rest.get()).toString());
	}

	// TODO: Task 5 - request handler

	// Inserts the comment into the restaurant
	@PostMapping(path="restaurant/comment", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> postRestaurantComments(@RequestBody MultiValueMap<String, String> form) {

			Comment c = new Comment();
			c.setRestaurantId(form.getFirst("restaurantId"));
			c.setName(form.getFirst("name"));
			c.setDate(new java.util.Date().getTime());
			c.setComment(form.getFirst("comment"));
			c.setRating(Integer.parseInt(form.getFirst("rating")));

			restaurantService.postRestaurantComment(c);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(Json.createObjectBuilder().build().toString());
	}
	// }
}
