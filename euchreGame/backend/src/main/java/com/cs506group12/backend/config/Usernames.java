package com.cs506group12.backend.config;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Generates random usernames from a predefined list.
 * 
 * @author jaime zepeda
 */
public class Usernames {
    // Predefined list of usernames
    private static List<String> usernames = Arrays.asList(
            "Lion", "Tiger", "Bear", "Elephant", "Giraffe",
            "Zebra", "Hippo", "Kangaroo", "Rhino", "Penguin",
            "Panda", "Koala", "Leopard", "Cheetah", "Jaguar",
            "Wolf", "Fox", "Deer", "Antelope", "Gorilla",
            "Monkey", "Chimpanzee", "Orangutan", "Sloth",
            "Cheetah", "Hyena", "Bison", "Buffalo", "Elk",
            "Moose", "Camel", "Llama", "Alpaca", "Gazelle",
            "Ostrich", "Emu", "Eagle", "Hawk", "Falcon",
            "Sparrow", "Raven", "Crow", "Parrot", "Macaw",
            "Flamingo", "Pelican", "Swan", "Duck", "Goose",
            "Pigeon", "Seagull", "Albatross", "Robin", "Bluejay",
            "Cardinal", "Hummingbird", "Woodpecker", "Penguin",
            "Puffin", "Seal", "Walrus", "Dolphin", "Whale",
            "Shark", "Octopus", "Squid", "Jellyfish", "Starfish",
            "Crab", "Lobster", "Shrimp", "Snail", "Slug",
            "Oyster", "Clam", "Mussel", "Scallop", "Barnacle",
            "Crocodile", "Alligator", "Turtle", "Tortoise", "Snake",
            "Python", "Boa", "Anaconda", "Cobra", "Viper",
            "Rattlesnake", "Mongoose", "Meerkat", "Ferret", "Otter",
            "Beaver", "Hedgehog", "Porcupine", "Armadillo", "Squirrel");

    /**
     * Generates a random username from the predefined list.
     *
     * @return A randomly selected username.
     */
    public static String getRandomUsername() {
        Random random = new Random();
        int randomIndex = random.nextInt(usernames.size());
        return usernames.get(randomIndex);
    }
}
