package com.edustocks.service;

import com.edustocks.model.Lesson;
import com.edustocks.model.Question;
import com.edustocks.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ProgressService progressService;

    public List<Lesson> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        if (lessons.isEmpty()) {
            initializeDefaultLessons();
            lessons = lessonRepository.findAll();
        }
        return lessons;
    }

    public List<Lesson> getLessonsByLevel(String level) {
        List<Lesson> allLessons = getAllLessons();
        return allLessons.stream()
            .filter(lesson -> lesson.getLevel().equalsIgnoreCase(level))
            .collect(Collectors.toList());
    }

    public Lesson getLessonById(String id) {
        Lesson lesson = lessonRepository.findById(id);
        if (lesson == null) {
            // Initialize lessons if not found
            initializeDefaultLessons();
            lesson = lessonRepository.findById(id);
        }
        return lesson;
    }

    public void completeLesson(String userId, String lessonId, double score) {
        progressService.completeLesson(userId, lessonId, score);
    }

    // Initialize default lessons
    public void initializeDefaultLessons() {
        if (lessonRepository.findAll().isEmpty()) {
            List<Lesson> defaultLessons = createDefaultLessons();
            for (Lesson lesson : defaultLessons) {
                lessonRepository.save(lesson);
            }
        }
    }

    private List<Lesson> createDefaultLessons() {
        List<Lesson> lessons = new ArrayList<>();

        // Beginner Lessons
        lessons.add(createBeginnerLesson1());
        lessons.add(createBeginnerLesson2());
        lessons.add(createBeginnerLesson3());

        // Intermediate Lessons
        lessons.add(createIntermediateLesson1());
        lessons.add(createIntermediateLesson2());
        lessons.add(createIntermediateLesson3());

        // Advanced Lessons
        lessons.add(createAdvancedLesson1());
        lessons.add(createAdvancedLesson2());
        lessons.add(createAdvancedLesson3());
        
        return lessons;
    }

    private Lesson createBeginnerLesson1() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-1");
        lesson.setTitle("What is a Stock?");
        lesson.setDescription("Learn the basics of stocks and how they work");
        lesson.setLevel("beginner");
        lesson.setContent("<h2>What is a Stock?</h2><p>A stock represents partial ownership in a company. When you buy a stock, you become a shareholder.</p>");
        lesson.setOrder(1);
        lesson.setQuestions(createQuestionsForLesson1());
        return lesson;
    }

    private Lesson createBeginnerLesson2() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-2");
        lesson.setTitle("Reading Stock Prices");
        lesson.setDescription("Understand how to read and interpret stock prices");
        lesson.setLevel("beginner");
        lesson.setContent("<h2>Reading Stock Prices</h2><p>Stock prices fluctuate based on supply and demand. Key metrics include opening price, closing price, and daily change.</p>");
        lesson.setOrder(2);
        lesson.setQuestions(createQuestionsForLesson2());
        return lesson;
    }

    private Lesson createBeginnerLesson3() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-3");
        lesson.setTitle("Stock Market Basics");
        lesson.setDescription("Learn how the stock market operates");
        lesson.setLevel("beginner");
        lesson.setContent("<h2>Stock Market Basics</h2><p>The stock market is where shares of companies are traded. It's regulated and provides liquidity for investors.</p>");
        lesson.setOrder(3);
        lesson.setQuestions(createQuestionsForLesson3());
        return lesson;
    }

    private Lesson createIntermediateLesson1() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-4");
        lesson.setTitle("Fundamental Analysis");
        lesson.setDescription("Analyze companies using financial statements");
        lesson.setLevel("intermediate");
        lesson.setContent("<h2>Fundamental Analysis</h2><p>Fundamental analysis involves examining a company's financial statements including income statements, balance sheets, and cash flow statements.</p>");
        lesson.setOrder(1);
        lesson.setQuestions(createQuestionsForLesson4());
        return lesson;
    }

    private Lesson createIntermediateLesson2() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-5");
        lesson.setTitle("Technical Analysis");
        lesson.setDescription("Understand price charts and trading patterns");
        lesson.setLevel("intermediate");
        lesson.setContent("<h2>Technical Analysis</h2><p>Technical analysis uses historical price data and charts to predict future price movements. Common indicators include moving averages and relative strength index (RSI).</p>");
        lesson.setOrder(2);
        lesson.setQuestions(createQuestionsForLesson5());
        return lesson;
    }

    private Lesson createIntermediateLesson3() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-6");
        lesson.setTitle("Portfolio Management");
        lesson.setDescription("Build and manage a diversified portfolio");
        lesson.setLevel("intermediate");
        lesson.setContent("<h2>Portfolio Management</h2><p>A well-managed portfolio should be diversified across different sectors and asset types to minimize risk and maximize returns.</p>");
        lesson.setOrder(3);
        lesson.setQuestions(createQuestionsForLesson6());
        return lesson;
    }

    private Lesson createAdvancedLesson1() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-7");
        lesson.setTitle("Options Trading");
        lesson.setDescription("Advanced derivatives trading strategies");
        lesson.setLevel("advanced");
        lesson.setContent("<h2>Options Trading</h2><p>Options give the holder the right, but not obligation, to buy or sell at a predetermined price. Strategies include calls, puts, spreads, and straddles.</p>");
        lesson.setOrder(1);
        lesson.setQuestions(createQuestionsForLesson7());
        return lesson;
    }

    private Lesson createAdvancedLesson2() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-8");
        lesson.setTitle("Risk Management");
        lesson.setDescription("Master advanced risk management techniques");
        lesson.setLevel("advanced");
        lesson.setContent("<h2>Risk Management</h2><p>Advanced risk management includes position sizing, stop-loss orders, correlation analysis, and value at risk (VaR) calculations.</p>");
        lesson.setOrder(2);
        lesson.setQuestions(createQuestionsForLesson8());
        return lesson;
    }

    private Lesson createAdvancedLesson3() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-9");
        lesson.setTitle("Market Psychology");
        lesson.setDescription("Understand investor behavior and market dynamics");
        lesson.setLevel("advanced");
        lesson.setContent("<h2>Market Psychology</h2><p>Market psychology examines how emotions drive market movements, including fear, greed, herding behavior, and contrarian thinking.</p>");
        lesson.setOrder(3);
        lesson.setQuestions(createQuestionsForLesson9());
        return lesson;
    }

    private List<Question> createQuestionsForLesson1() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q1");
        q1.setQuestion("What does owning a stock mean?");
        q1.setOptions(List.of(
            "You own a piece of the company",
            "You lent money to the company",
            "You are an employee of the company",
            "You are a customer of the company"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Owning a stock means you own a share of the company, making you a partial owner.");
        questions.add(q1);

        return questions;
    }

    private List<Question> createQuestionsForLesson2() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q2-1");
        q1.setQuestion("If a stock opens at $100 and closes at $105, what is the change?");
        q1.setOptions(List.of("$5", "$5%", "5%", "$-5"));
        q1.setCorrectAnswer(0);
        q1.setExplanation("The change is $105 - $100 = $5");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson3() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q3-1");
        q1.setQuestion("What is the primary purpose of the stock market?");
        q1.setOptions(List.of(
            "To allow companies to raise capital and investors to own portions of companies",
            "To set government policy",
            "To control inflation",
            "To replace banking systems"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("The stock market allows companies to raise capital by selling shares and provides investors with ownership opportunities.");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson4() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q4-1");
        q1.setQuestion("Which metric measures a company's profitability?");
        q1.setOptions(List.of("Price-to-Earnings Ratio", "Debt-to-Equity Ratio", "Price-to-Book Ratio", "Current Ratio"));
        q1.setCorrectAnswer(0);
        q1.setExplanation("P/E ratio compares price to earnings, measuring profitability relative to stock price.");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson5() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q5-1");
        q1.setQuestion("What does a moving average help identify?");
        q1.setOptions(List.of("Price trends", "Company earnings", "Government policies", "Employee satisfaction"));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Moving averages smooth out price data to help identify trends over time.");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson6() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q6-1");
        q1.setQuestion("Why is diversification important?");
        q1.setOptions(List.of(
            "To reduce risk by spreading investments across different assets",
            "To maximize returns every year",
            "To avoid paying taxes",
            "To guarantee profits"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Diversification reduces overall portfolio risk by not putting all investments in one asset.");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson7() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q7-1");
        q1.setQuestion("What gives the holder the right to buy at a predetermined price?");
        q1.setOptions(List.of("Call option", "Put option", "Forward contract", "Futures contract"));
        q1.setCorrectAnswer(0);
        q1.setExplanation("A call option gives the right to buy at a specific price (strike price).");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson8() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q8-1");
        q1.setQuestion("What is Value at Risk (VaR) used for?");
        q1.setOptions(List.of(
            "To measure potential losses under normal conditions",
            "To guarantee no losses",
            "To increase leverage",
            "To reduce trading fees"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("VaR estimates the maximum potential loss over a specific time period at a given confidence level.");
        questions.add(q1);
        return questions;
    }

    private List<Question> createQuestionsForLesson9() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId("q9-1");
        q1.setQuestion("What is herding behavior in markets?");
        q1.setOptions(List.of(
            "When investors follow the crowd's investment decisions",
            "When companies hire more employees",
            "When stock prices decrease",
            "When government increases taxes"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Herding behavior occurs when investors make decisions based on others' actions rather than independent analysis.");
        questions.add(q1);
        return questions;
    }
}

