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

        // Beginner Lessons (5 lessons)
        lessons.add(createBeginnerLesson1());
        lessons.add(createBeginnerLesson2());
        lessons.add(createBeginnerLesson3());
        lessons.add(createBeginnerLesson4());
        lessons.add(createBeginnerLesson5());

        // Intermediate Lessons (5 lessons)
        lessons.add(createIntermediateLesson1());
        lessons.add(createIntermediateLesson2());
        lessons.add(createIntermediateLesson3());
        lessons.add(createIntermediateLesson4());
        lessons.add(createIntermediateLesson5());

        // Advanced Lessons (5 lessons)
        lessons.add(createAdvancedLesson1());
        lessons.add(createAdvancedLesson2());
        lessons.add(createAdvancedLesson3());
        lessons.add(createAdvancedLesson4());
        lessons.add(createAdvancedLesson5());
        
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

    private Lesson createBeginnerLesson4() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-10");
        lesson.setTitle("Dividends and Earnings");
        lesson.setDescription("Understand how companies share profits with shareholders");
        lesson.setLevel("beginner");
        lesson.setContent("<h2>Dividends and Earnings</h2><p>Dividends are portions of company profits paid to shareholders. Earnings are the company's net profit. Understanding these helps you evaluate investment opportunities.</p><h3>Key Points:</h3><ul><li>Dividends provide regular income</li><li>Earnings growth drives stock price appreciation</li><li>Dividend yield is annual dividend divided by stock price</li></ul>");
        lesson.setOrder(4);
        lesson.setQuestions(createQuestionsForLesson10());
        return lesson;
    }

    private Lesson createBeginnerLesson5() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-11");
        lesson.setTitle("Getting Started with Investing");
        lesson.setDescription("First steps to begin your investing journey");
        lesson.setLevel("beginner");
        lesson.setContent("<h2>Getting Started with Investing</h2><p>Before you start investing, understand the basics of accounts, risk tolerance, and investment strategies.</p><h3>Steps to Begin:</h3><ul><li>Choose a brokerage account</li><li>Assess your risk tolerance</li><li>Start with small amounts</li><li>Diversify your portfolio</li><li>Keep learning continuously</li></ul>");
        lesson.setOrder(5);
        lesson.setQuestions(createQuestionsForLesson11());
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

    private Lesson createIntermediateLesson4() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-12");
        lesson.setTitle("Valuation Methods and P/E Ratios");
        lesson.setDescription("Learn how to value stocks and evaluate fair prices");
        lesson.setLevel("intermediate");
        lesson.setContent("<h2>Valuation Methods and P/E Ratios</h2><p>Valuation is crucial for identifying undervalued or overvalued stocks. The P/E ratio is one of the most important metrics.</p><h3>Valuation Methods:</h3><ul><li>Price-to-Earnings (P/E) Ratio: Compare stock price to earnings</li><li>Price-to-Book (P/B) Ratio: Compare market value to book value</li><li>Price-to-Sales (P/S) Ratio: Compare price to revenue</li><li>Discounted Cash Flow (DCF): Estimates intrinsic value</li></ul>");
        lesson.setOrder(4);
        lesson.setQuestions(createQuestionsForLesson12());
        return lesson;
    }

    private Lesson createIntermediateLesson5() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-13");
        lesson.setTitle("Market Indices and Benchmarking");
        lesson.setDescription("Understand market indices and how to measure performance");
        lesson.setLevel("intermediate");
        lesson.setContent("<h2>Market Indices and Benchmarking</h2><p>Market indices track overall market performance. Benchmarking helps you evaluate your portfolio's performance against standards.</p><h3>Major Indices:</h3><ul><li>S&P 500: 500 large-cap U.S. stocks</li><li>Dow Jones Industrial Average: 30 blue-chip stocks</li><li>NASDAQ: Technology-heavy index</li><li>Russell 2000: 2000 small-cap stocks</li></ul>");
        lesson.setOrder(5);
        lesson.setQuestions(createQuestionsForLesson13());
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

    private Lesson createAdvancedLesson4() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-14");
        lesson.setTitle("Derivatives and Hedging Strategies");
        lesson.setDescription("Master advanced hedging and derivative strategies");
        lesson.setLevel("advanced");
        lesson.setContent("<h2>Derivatives and Hedging Strategies</h2><p>Derivatives like options and futures can be used to hedge risks or speculate. Understanding these instruments is crucial for advanced traders.</p><h3>Hedging Strategies:</h3><ul><li>Long Straddle: Profits from large price movements</li><li>Protective Put: Insurance against downside</li><li>Covered Call: Generate income from holdings</li><li>Iron Condor: Profit from price stability</li><li>Collar Strategy: Limit losses while capping gains</li></ul>");
        lesson.setOrder(4);
        lesson.setQuestions(createQuestionsForLesson14());
        return lesson;
    }

    private Lesson createAdvancedLesson5() {
        Lesson lesson = new Lesson();
        lesson.setId("lesson-15");
        lesson.setTitle("Macroeconomic Analysis and Global Markets");
        lesson.setDescription("Analyze macroeconomic factors affecting global markets");
        lesson.setLevel("advanced");
        lesson.setContent("<h2>Macroeconomic Analysis and Global Markets</h2><p>Understanding macroeconomic indicators and global events is essential for predicting market trends and currency movements.</p><h3>Key Indicators:</h3><ul><li>GDP Growth: Overall economic health</li><li>Interest Rates: Impact on valuations and borrowing</li><li>Inflation: Affects purchasing power and valuations</li><li>Employment Data: Indicator of economic strength</li><li>Geopolitical Events: Can cause market volatility</li><li>Currency Exchange Rates: Affect international investments</li></ul>");
        lesson.setOrder(5);
        lesson.setQuestions(createQuestionsForLesson15());
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

    private List<Question> createQuestionsForLesson10() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q10-1");
        q1.setQuestion("What is a dividend?");
        q1.setOptions(List.of(
            "A portion of company profits paid to shareholders",
            "The company's total revenue",
            "The stock price increase",
            "The fee charged by brokers"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Dividends are portions of company earnings distributed to shareholders as a reward for ownership.");
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q10-2");
        q2.setQuestion("If a company stock is $100 and pays $4 annual dividend, what is the dividend yield?");
        q2.setOptions(List.of("4%", "25%", "0.04%", "40%"));
        q2.setCorrectAnswer(0);
        q2.setExplanation("Dividend yield = Annual dividend / Stock price = $4 / $100 = 4%");
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q10-3");
        q3.setQuestion("What do company earnings represent?");
        q3.setOptions(List.of(
            "The company's net profit after all expenses",
            "The company's total revenue",
            "The stock trading volume",
            "The company's market value"
        ));
        q3.setCorrectAnswer(0);
        q3.setExplanation("Earnings are the company's net profit after subtracting all expenses, taxes, and costs from revenue.");
        questions.add(q3);

        return questions;
    }

    private List<Question> createQuestionsForLesson11() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q11-1");
        q1.setQuestion("What is risk tolerance?");
        q1.setOptions(List.of(
            "Your ability to endure fluctuations in investment values",
            "The amount of money you can invest",
            "The skill to predict stock prices",
            "The number of stocks in your portfolio"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Risk tolerance is your psychological and financial ability to handle ups and downs in your investment values.");
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q11-2");
        q2.setQuestion("Why should beginners start with small investments?");
        q2.setOptions(List.of(
            "To learn with minimal risk while gaining experience",
            "Because they won't make money",
            "Because stocks are always risky",
            "To avoid paying taxes"
        ));
        q2.setCorrectAnswer(0);
        q2.setExplanation("Starting small allows you to learn market dynamics and build confidence without risking significant capital.");
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q11-3");
        q3.setQuestion("What is diversification for beginners?");
        q3.setOptions(List.of(
            "Spreading investments across different stocks and sectors",
            "Investing all money in one company",
            "Buying the same stock multiple times",
            "Only investing in technology stocks"
        ));
        q3.setCorrectAnswer(0);
        q3.setExplanation("Diversification means spreading your investments across different companies and sectors to reduce overall risk.");
        questions.add(q3);

        return questions;
    }

    private List<Question> createQuestionsForLesson12() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q12-1");
        q1.setQuestion("What does a P/E ratio of 20 mean?");
        q1.setOptions(List.of(
            "Investors pay $20 for every $1 of earnings",
            "The stock will increase 20% next year",
            "The dividend yield is 20%",
            "The company profits $20 per share"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("P/E ratio = Stock Price / Earnings Per Share. A P/E of 20 means investors are willing to pay $20 for every $1 of annual earnings.");
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q12-2");
        q2.setQuestion("Which valuation method estimates intrinsic value using future cash flows?");
        q2.setOptions(List.of(
            "Discounted Cash Flow (DCF)",
            "Price-to-Book Ratio",
            "Price-to-Sales Ratio",
            "P/E Multiple"
        ));
        q2.setCorrectAnswer(0);
        q2.setExplanation("DCF analysis values a company based on the present value of its expected future cash flows.");
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q12-3");
        q3.setQuestion("A stock with a lower P/E ratio is always a better investment. True or False?");
        q3.setOptions(List.of(
            "False",
            "True"
        ));
        q3.setCorrectAnswer(0);
        q3.setExplanation("False. A lower P/E might indicate undervaluation, but it could also indicate poor growth prospects or higher risk. Always analyze in context.");
        questions.add(q3);

        return questions;
    }

    private List<Question> createQuestionsForLesson13() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q13-1");
        q1.setQuestion("What does the S&P 500 index track?");
        q1.setOptions(List.of(
            "500 large-cap U.S. stocks",
            "5000 small-cap stocks",
            "50 technology stocks",
            "500 international stocks"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("The S&P 500 tracks 500 large-cap U.S. stocks and is considered the best indicator of overall U.S. stock market health.");
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q13-2");
        q2.setQuestion("Why is it important to benchmark your portfolio?");
        q2.setOptions(List.of(
            "To compare your returns against market standards and assess performance",
            "To copy the index exactly",
            "To guarantee higher returns",
            "To avoid paying taxes"
        ));
        q2.setCorrectAnswer(0);
        q2.setExplanation("Benchmarking helps you evaluate whether your portfolio is performing well compared to relevant market indices.");
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q13-3");
        q3.setQuestion("Which index is technology-heavy?");
        q3.setOptions(List.of(
            "NASDAQ",
            "Dow Jones Industrial Average",
            "Russell 2000",
            "Nikkei 225"
        ));
        q3.setCorrectAnswer(0);
        q3.setExplanation("NASDAQ is concentrated in technology stocks and includes companies like Apple, Microsoft, and Amazon.");
        questions.add(q3);

        return questions;
    }

    private List<Question> createQuestionsForLesson14() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q14-1");
        q1.setQuestion("What is a protective put?");
        q1.setOptions(List.of(
            "An option strategy that insures against stock price declines",
            "A strategy to increase stock price",
            "A way to avoid market volatility",
            "A type of stock dividend"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("A protective put involves buying a put option on a stock you own to protect against significant price declines.");
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q14-2");
        q2.setQuestion("What is a covered call strategy primarily used for?");
        q2.setOptions(List.of(
            "Generating income from existing stock holdings",
            "Protecting against large price declines",
            "Betting on stock price increases",
            "Avoiding dividend payments"
        ));
        q2.setCorrectAnswer(0);
        q2.setExplanation("A covered call involves selling call options on stocks you own, generating premium income while maintaining the stock position.");
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q14-3");
        q3.setQuestion("What does an iron condor strategy profit from?");
        q3.setOptions(List.of(
            "Stock price remaining stable within a range",
            "Large price movements",
            "Rapid trading",
            "Dividend increases"
        ));
        q3.setCorrectAnswer(0);
        q3.setExplanation("An iron condor is a neutral strategy that profits when the stock price stays within a defined range between the strike prices.");
        questions.add(q3);

        return questions;
    }

    private List<Question> createQuestionsForLesson15() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q15-1");
        q1.setQuestion("How does GDP growth affect stock markets?");
        q1.setOptions(List.of(
            "Strong GDP growth typically leads to higher corporate profits and stock prices",
            "GDP growth has no effect on stocks",
            "GDP growth always decreases stock prices",
            "Only technology stocks are affected by GDP"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Strong GDP growth indicates economic expansion, leading to increased corporate profits, higher consumer spending, and rising stock valuations.");
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q15-2");
        q2.setQuestion("What happens to stock valuations when interest rates increase?");
        q2.setOptions(List.of(
            "Valuations typically decrease because future cash flows are worth less",
            "Valuations always increase",
            "Interest rates don't affect valuations",
            "Only bond prices are affected"
        ));
        q2.setCorrectAnswer(0);
        q2.setExplanation("Higher interest rates reduce the present value of future cash flows, making stocks less attractive relative to bond investments.");
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q15-3");
        q3.setQuestion("How can geopolitical events impact global stock markets?");
        q3.setOptions(List.of(
            "They can cause volatility and uncertainty, affecting investor sentiment and market prices",
            "Geopolitical events never affect markets",
            "Only local markets are affected",
            "Only commodity prices are affected"
        ));
        q3.setCorrectAnswer(0);
        q3.setExplanation("Geopolitical tensions, trade conflicts, and international events create uncertainty and volatility that ripple through global markets.");
        questions.add(q3);

        return questions;
    }
}

