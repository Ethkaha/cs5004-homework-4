# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts.

## Technical Questions

1. Describe the purpose of a model in the MVC architecture. What is the model responsible for? What are some examples of what might be included in a model?

    The model in MVC is responsible for storing an managing the data of the application. It also handles any logic related to that data like calculations or updates. For example, in a calculator app, the model would include methods like add, subtract, multiply, and divide. The model doesn't deal with how things are displayed, only how the data is handled.

 ---

2. Describe the purpose of a controller in the MVC architecture. What is the controller responsible for? What are some examples of what might be included in a controller?

    The controller is responsible for handling a users input and connecting the model and the view. It takes the input from the user like button clicks or typed commands and decides what actions should be performed. The controller then calls the appropriate methods in the model and updates the view with the results. For example, if a user enters "+ 2 3", the controller would parse that input, call the add method in the model, and display the result of it. That makes the controller act as the middle layer that manages the interaction between the user and the system.

---

3. Describe the word serialization, and how it relates to 'data-binding' in Jackson. What is the purpose of serialization? What is the purpose of data-binding? What is the relationship between the two?

    Serialization is the process of converting an object into a format that can be stored or transmitted, like JSON. This allows data to be saved to a file or sent over a network. Data-binding in Jackson is the automatic process of converting between Java objects and formats like JSON. It includes both serialization (object to JSON) and deserialization (JSON back to object). The purpose of serialization is to make data portable, while data-binding simplifies the process by handling the conversion automatically. So looking at it more simply, serialization is one part of data-binding.

---

4. Describe the differences between JSON and CSV - make sure to reference flat or hierarchical data in your answer. What are some advantages of JSON over CSV? What are some disadvantages?

    JSON and CSV are both formats used to store and transfer data, but they differ in structure. JSON supports hierarchical (nested) data, meaning it can store objects within objects or arrays. CSV, on the other hand, is designed for flat data, where information is stored in rows and columns. One advantage of JSON is that it's more flexible and can represent complex data structures, which makes it useful for APIs and modern applications. However, JSON files are usually larger and more complex to read while CSV is  more simple and easier to use with tools like Excel, but it can't handle nested data well.

---

5. Why would we want to use InputStreams and OutputStreams throughout a program instead of specific types of streams (for most cases)?

    InputStreams and OutputStreams provide a general way to handle input and output in a program. Instead of just using a specific type of stream for each situation, these allow you to work with many data sources in a consistent way like network connections, or memory. This makes the program more flexible and easier to maintain. For example, you could switch from reading a file to reading from a network without changing much of your code. Using streams also allows you to chain them together for more advanced functionality.

---

6. What is the difference between a record and a class in Java? When might you use one over the other?

    A class in Java is a general structure that can store data and include methods, and it allows for full customization and behavior. A record, however, is a simpler type designed mainly for storing data. Records automatically provide a constructor, getters, and methods like `toString()`, `equals()`, and `hashCode()`. They are also immutable, meaning their values can't be changed after their creation. You would use a record when you only need to store data without extra logic. A class is better when you need more control like modifying data or adding complex behavior.

---

7. What is a java "bean"?

    A Java Bean is a class that follows a specific structure so it can be easily used by frameworks and tools. It usually has private variables, public getter and setter methods, and a no-argument constructor. Java Beans are often used to store and transfer data between parts of a program. For example, a User class with fields like name and age, along with getters and setters, would be considered a Java Bean. This standard structure makes it easier for libraries and frameworks to work with the class automatically.

---

## Deeper Thinking

The data for this assignment was downloaded from ipapi.co, and the data itself is publicly listed. For the pervious assignment, we used data from Board Game Geek, which a person has a unofficial collection of the BGArena games. It is also worth noting it is actually out of date, since BGGeek has added a category for Digital Implementations of games.

Data of many types are  often available online (Here is a list of a bunch of [free API](https://mixedanalytics.com/blog/list-actually-free-open-no-auth-needed-apis/)s), and even the owners of Board Game Geek have RPGGeek and VideoGameGeek. To try out Board Game geeks API, you can put into the url https://www.boardgamegeek.com/xmlapi2/thing?id=13 and you will get back an XML file with information about the game with id 13.

Take some time to find other online data APIs that you might be interested in. What kind of data would you like to work with? What kind of data would be useful for you to have access to? Another example of an API a random dog image API https://dog.ceo/dog-api/!

🔥 Find at least two other APIs/Data sources (so downloadable data is also valid). Link them, and provide a brief description of what kind of data they provide. These will act as your references for this assignment.

However, just because information is freely available online, it does not mean

* You have legal rights to that information
* The information was collected ethically
* The information is accurate
* The information is without bias

🔥 Take some time to think about the ethical implications of using data from the internet. What are some ways that you can ensure that the data you are using is accurate and unbiased? Provide some examples of key questions you might ask yourself before using data in a project, and what are some questions you can use to help you evaluate the data you are using. Please include references if you use any, as there are plenty of articles out there talking about this topic.

---

### Answer

If I were to choose APIs to work with, I would probably be most interested in video game and sports data, since these are areas I am familiar with and interested in. For example, the RAWG Video Games Database API (https://rawg.io/apidocs) provides detailed information about video games, including genres, platforms, ratings, and release dates [1]. This type of data could be used to build a recommendation system, track popular games, or analyze trends in the gaming industry. Another useful API is TheSportsDB (https://www.thesportsdb.com/api.php), which provides data about teams, players, and game results across many sports leagues [2]. This data could be used to build applications that track team performance, display statistics, or show recent game results. These APIs are especially useful because they are constantly updated and relevant to real-world interests.

Yet just because the data is freely available online doesn’t mean it is always safe or appropriate to use. There are important ethical concerns to consider when working with data. According to the UK government’s Data Ethics Framework, data quality and limitations should always be considered and clearly documented [3]. In addition, NIST explains that bias in data can come from many sources, including how the data was collected or which groups are represented [4]. For example, sports data could focus heavily on major leagues but ignore smaller teams or the minor leagues, while video game data might show the preferences of certain groups more than others. This can lead to biased results if not carefully considered.
To make sure data is accurate and unbiased, I would start by checking where the data comes from and how it was collected. I would also check how recent the data is and whether it’s missing important information and then comparing it with some other sources can help confirm the accuracy. It’s also important to review whether the data can be used responsibly and within ethical guidelines [3]. Google’s fairness guidelines also point out that missing or unbalanced data can introduce bias, so checking for representation across different groups is important [5].

Before using any dataset, I would ask myself a few key questions:

* Who created this data, and why?
* Is the data accurate and up to date?
* Are there any biases or missing groups?
* Could using this data lead to misleading conclusions?
* Is the data being used responsibly and ethically? [3] [4] [5]

Overall, video game and sports APIs provide useful and interesting data that can be used to build real-world applications. However, it is important to think critically about how the data was collected, whether it is accurate, and whether it is being used responsibly [3] [4].

---

### References

[1] RAWG. n.d. RAWG Video Games Database API. Retrieved March 23, 2026 from https://rawg.io/apidocs

[2] TheSportsDB. n.d. Free Sports API. Retrieved March 23, 2026 from https://www.thesportsdb.com/api.php

[3] UK Government. 2020. Data Ethics Framework. Retrieved March 23, 2026 from https://www.gov.uk/government/publications/data-ethics-framework

[4] National Institute of Standards and Technology (NIST). 2023. AI Risk Management Framework. Retrieved March 23, 2026 from https://www.nist.gov/itl/ai-risk-management-framework

[5] Google. n.d. Machine Learning Fairness Overview. Retrieved March 23, 2026 from https://developers.google.com/machine-learning/fairness-overview
