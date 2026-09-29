package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class helptool extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helptool);

        LinearLayout helpContentLayout = findViewById(R.id.help_content_layout);

        // Adding all questions and answers manually
        addHelpContent(helpContentLayout);
    }

    private void addHelpContent(LinearLayout layout) {
        String[] questions = {
                "How do I start the app?",
                "What are the core features of this app?",
                "How do I generate a floor plan?",
                "Can I modify the generated design?",
                "What is AI-powered design generation?",
                "Can the AI suggest materials for construction?",
                "How do I save the generated floor plan?",
                "How does AI optimize space in a floor plan?",
                "What is machine learning in architecture?",
                "What is the purpose of structural analysis in architecture?",
                "How can AI improve energy efficiency in buildings?",
                "What is a generative adversarial network (GAN)?",
                "How do I visualize my building design in 3D?",
                "Can AI help with interior design?",
                "How do I share my design with others?",
                "How does AI handle sustainability in architecture?",
                "What is the cost estimation feature?",
                "How does AI assist in urban planning?",
                "Can AI help with historical building preservation?",
                "What are smart building systems?",
                "How can AI assist with fire safety in buildings?",
                "What is an energy-efficient building design?",
                "How does AI help in material selection for construction?",
                "What is the significance of AI in construction scheduling?",
                "What is the role of AI in construction robotics?",
                "What does AI-powered project management mean?",
                "How does AI optimize space in small buildings?",
                "How can AI predict construction material shortages?",
                "What is the benefit of using AI in green building design?",
                "Can AI predict building lifespan?",
                "How does AI improve accessibility in building designs?",
                "What is AI's role in disaster-resistant architecture?",
                "How does AI help with noise reduction in buildings?",
                "Can AI help with zoning and land use planning?",
                "What is the role of AI in disaster recovery and reconstruction?",
                "How can AI aid in seismic design for buildings?",
                "What does AI-driven automated design mean?",
                "What are the challenges of implementing AI in architecture?",
                "How does AI manage risk in building construction?",
                "What is AI-based cost optimization?",
                "How can AI help with waste management in construction?",
                "Can AI help design buildings that are resilient to extreme weather?",
                "How does AI enhance the safety of construction sites?",
                "What is an AI-based energy audit?",
                "Can AI assist with multi-floor building designs?",
                "How does AI help with lighting design?",
                "What are AI-assisted construction materials?",
                "How does AI improve the quality of building designs?",
                "How can AI assist in integrating renewable energy systems in buildings?",
                "What is the significance of AI in smart homes?"
        };

        String[] answers = {
                "To start the app, simply tap on the app icon on your device.",
                "The core features include AI-powered design generation, machine learning, structural analysis, and sustainability planning.",
                "To generate a floor plan, input your preferences such as area, number of rooms, and layout type, then press 'Generate'.",
                "Yes, after generating the design, you can adjust the layout and other parameters manually.",
                "AI-powered design generation automatically creates architectural layouts based on user input and past designs.",
                "Yes, AI can recommend the best materials based on durability, cost, and sustainability.",
                "After generating the floor plan, you can save it by tapping on the 'Save' button on the screen.",
                "AI analyzes space utilization and recommends the most efficient placement for rooms, ensuring better flow and accessibility.",
                "Machine learning uses data from past designs to identify patterns and improve the accuracy of future design suggestions.",
                "Structural analysis ensures that the design can handle loads, is safe, and meets engineering standards.",
                "AI analyzes environmental data to suggest energy-efficient designs, such as optimal window placement, insulation, and ventilation.",
                "A GAN is a type of AI used to generate realistic images, such as 2D and 3D models of architectural designs.",
                "Once your floor plan is generated, AI can convert it into a 3D model for better visualization of the design.",
                "Yes, AI can suggest furniture arrangements, color schemes, lighting setups, and decor choices based on your preferences.",
                "You can share your design by selecting the 'Share' option and choosing your preferred sharing method, such as email or social media.",
                "AI ensures that the designs meet sustainability standards by recommending eco-friendly materials and energy-efficient systems.",
                "The cost estimation feature predicts the construction costs based on the materials, labor, and design specifications.",
                "AI helps in designing optimized city infrastructure by analyzing traffic flow, population density, and resource usage.",
                "Yes, AI can analyze old structures and recommend restoration methods that preserve historical accuracy while maintaining safety.",
                "Smart building systems are automated systems that use AI to optimize energy usage, security, and the overall management of the building.",
                "AI predicts fire risks and helps in designing escape routes, fire suppression systems, and ensuring compliance with safety standards.",
                "Energy-efficient buildings use design strategies and technologies to reduce energy consumption, such as proper insulation, natural lighting, and efficient HVAC systems.",
                "AI analyzes factors like durability, cost, environmental impact, and availability to suggest the best materials for a project.",
                "AI can predict project timelines, allocate resources, and optimize construction schedules to minimize delays and maximize efficiency.",
                "AI guides construction robots to perform tasks like bricklaying, welding, and painting with precision, reducing labor costs and errors.",
                "AI-powered project management uses algorithms to track progress, manage resources, and provide real-time updates to ensure timely project completion.",
                "AI can optimize layouts, ensuring efficient use of space without sacrificing functionality, and can recommend multi-purpose furniture and space-saving designs.",
                "AI analyzes historical data and market trends to predict potential shortages and recommend alternate suppliers or materials in advance.",
                "AI helps optimize designs to minimize environmental impact by suggesting energy-efficient materials, sustainable construction methods, and eco-friendly energy sources.",
                "Yes, AI can analyze materials and environmental factors to estimate the lifespan of a building and suggest improvements for durability.",
                "AI can ensure that designs meet accessibility standards by recommending features like ramps, elevators, and wider doorways for better mobility.",
                "AI can analyze historical disaster data and use this information to design buildings that are resistant to earthquakes, floods, and other natural disasters.",
                "AI suggests materials and layout modifications that reduce noise pollution, such as acoustic panels and soundproofing techniques.",
                "Yes, AI can analyze geographical data and provide optimal land use solutions, balancing residential, commercial, and recreational spaces.",
                "AI can assess damage after a disaster and generate rebuilding plans that prioritize safety, sustainability, and cost-effectiveness.",
                "AI uses data from previous earthquakes to optimize the design of buildings, ensuring that they can withstand seismic forces.",
                "AI-driven automated design allows for the generation of architectural layouts without human intervention, based on specified parameters and historical data.",
                "Challenges include the high initial investment, the need for accurate data, and ensuring that AI solutions align with human creativity and design preferences.",
                "AI predicts risks such as cost overruns, project delays, and safety hazards, providing solutions to mitigate these issues in real time.",
                "AI-based cost optimization uses algorithms to find the most cost-effective solutions for building designs, materials, and construction processes.",
                "AI tracks waste production during construction and recommends ways to reduce, recycle, and reuse materials to minimize environmental impact.",
                "Yes, AI can design buildings that withstand extreme weather conditions such as hurricanes, tornadoes, and heatwaves.",
                "AI uses sensors and cameras to monitor construction sites for safety hazards, providing real-time alerts to prevent accidents.",
                "An AI-based energy audit analyzes a building's energy usage and recommends improvements to reduce consumption and optimize energy efficiency.",
                "Yes, AI can generate optimized designs for multi-floor buildings, ensuring that each floor maximizes space utilization and energy efficiency.",
                "AI analyzes natural light patterns and recommends optimal window placement and artificial lighting solutions to reduce energy consumption.",
                "AI-assisted construction materials are those that are recommended by AI based on factors like sustainability, strength, cost, and environmental impact.",
                "AI ensures that designs adhere to quality standards, while also recommending enhancements to improve functionality, aesthetics, and sustainability.",
                "AI analyzes energy demands and suggests the integration of solar panels, wind turbines, or geothermal systems to reduce dependency on non-renewable sources.",
                "AI plays a crucial role in smart homes by optimizing energy usage, security, and automation of household functions."
        };

        // Adding all questions and answers
        for (int i = 0; i < questions.length; i++) {
            TextView questionTextView = new TextView(this);
            questionTextView.setText(questions[i]);
            questionTextView.setTextSize(18);
            questionTextView.setGravity(Gravity.START);
            questionTextView.setPadding(0, 16, 0, 8);

            TextView answerTextView = new TextView(this);
            answerTextView.setText(answers[i]);
            answerTextView.setTextSize(16);
            answerTextView.setGravity(Gravity.START);
            answerTextView.setPadding(0, 0, 0, 16);

            // Add to layout
            layout.addView(questionTextView);
            layout.addView(answerTextView);
        }
    }
}
