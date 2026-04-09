package com.example.quizapp

object QuizData {
    val questions = listOf(
        Question("What is the capital of Australia?",
            listOf("Sydney", "Melbourne", "Canberra", "Brisbane"), 2),
        Question("Which planet is known as the Red Planet?",
            listOf("Venus", "Mars", "Jupiter", "Saturn"), 1),
        Question("What is the largest ocean on Earth?",
            listOf("Atlantic", "Indian", "Arctic", "Pacific"), 3),
        Question("Who wrote 'Romeo and Juliet'?",
            listOf("Dickens", "Shakespeare", "Austen", "Twain"), 1),
        Question("What is the chemical symbol for gold?",
            listOf("Go", "Gd", "Au", "Ag"), 2),
        Question("How many continents are there on Earth?",
            listOf("5", "6", "7", "8"), 2),
        Question("What is the approximate speed of light?",
            listOf("150,000 km/s", "300,000 km/s", "450,000 km/s", "600,000 km/s"), 1),
        Question("Which language is primarily used to develop Android apps?",
            listOf("Swift", "Python", "Kotlin", "Ruby"), 2),
        Question("What is the largest mammal in the world?",
            listOf("African Elephant", "Blue Whale", "Giraffe", "Great White Shark"), 1),
        Question("In which year did World War II end?",
            listOf("1943", "1944", "1945", "1946"), 2)
    )
}
