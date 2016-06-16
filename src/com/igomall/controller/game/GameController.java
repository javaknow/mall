
package com.igomall.controller.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("gameController")
@RequestMapping("/game")
public class GameController extends BaseController {

	@RequestMapping(value = "/{page1}/{page}", method = RequestMethod.GET)
	public String list(@PathVariable String page1, @PathVariable String page) {
		return "/game/"+page1+"/"+page;
	}

}