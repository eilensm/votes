package de.itemis.bonn.microservices.api.rest;

import de.itemis.bonn.rating.Item;
import de.itemis.bonn.rating.Vote;
import de.itemis.bonn.rating.VotingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/items")
public class ItemResource {

  private final VotingService votingService;

  public ItemResource(final VotingService votingService) {
    this.votingService = votingService;
  }

  @RequestMapping(method = POST)
  public Item createItem(@RequestBody final Item item) {
    return votingService.createItem(item.getDescription());
  }

  @RequestMapping(path = "/{itemId}/votes", method = POST)
  public Item vote(@PathVariable final String itemId, @RequestBody final Vote vote) {
    return votingService.vote(itemId, vote);
  }

  @RequestMapping(path = "/{id}", method = GET)
  public @ResponseBody
  Item getItem(@PathVariable final String id) {
    return votingService.getItem(id);
  }

  @RequestMapping(method = GET)
  public @ResponseBody
  List<Item> getItems() {
    return votingService.getItems();
  }

}
