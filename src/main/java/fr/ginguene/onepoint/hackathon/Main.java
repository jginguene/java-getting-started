/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.ginguene.onepoint.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.ginguene.onepoint.hackathon.bot.Bot5;

@RestController
@SpringBootApplication
public class Main {

	private long partie = -1;

	private InputParser parser = new InputParser();
	private static IBot bot;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bot(@RequestBody String input) {

		Carte carte = parser.parse(input);

		if (carte.getConfiguration().getIdentifiantPartie() != partie) {
			partie = carte.getConfiguration().getIdentifiantPartie();
			Carte.clear();
			bot = new Bot5();
		}

		Response response = bot.getResponse(carte);
		System.out.println("Response:" + response.toString());
		return response.toString();
	}

}
