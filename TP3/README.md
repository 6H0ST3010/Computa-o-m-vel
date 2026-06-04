# Tutorial 3-JP Compose

Course: LEIC\
Student(s): Rodrigo Amaral\
Date: 4/6/26\
Repository URL: [https://github.com/6H0ST3010/Computacao_movel/TP3](https://github.com/6H0ST3010/Computacao_movel/tree/main/TP3)
---
## 1. Introduction
The purpose of this assignment was to enhance advanced proficiency in the Kotlin programming language and master contemporary Android system development paradigms through a two-fold engineering workflow.
The primary objectives focused on:
1. Compile-Time Metaprogramming: Building a multi-module Kotlin JVM system utilizing custom annotations, the Kotlin Annotation Processing Tool (kapt), and declarative code generation libraries (KotlinPoet) to automate repetitive task structures without runtime reflection overhead.
2. Reactive Mobile Development: Re-architecting a real-time meteorological tracking system under the Model-View-ViewModel (MVVM) software pattern using Jetpack Compose. This component requires asynchronous data consumption from the remote Open-Meteo REST service, seamless state management during device lifecycle orientation changes, granular user interface (UI) decomposition into highly atomic, reusable layout files, and dynamic internationalization.

## 2. System Overview
The finalized software delivery is composed of two distinct operational systems:
### 2.1. Annotation Processor
An abstract automation utility that processes code syntax flags at compile time to dynamically generate decorator classes using composition: 
- @Greeting Processing Engine: Automatically identifies annotated methods and builds public, final wrapper classes that inject specialized greeting messages directly prior to executing original method logic.
- Core Use Cases: Automating tracing/logging blocks across disparate classes, decoupling diagnostic concerns from business logic, and proving compile-time generation workflows without performance degradation.

### 2.2. Cool Weather Mobile Application
A responsive Android mobile application that provides live atmospheric telemetry tracking based on coordinates:
- Main Features: Asynchronous network parsing via Ktor and Kotlinx Serialization, dynamic daytime/nighttime background transitions, reactive translation of WMO codes into vector drawables, and an input validation framework.
- Core Use Cases: Real-time fetching of localized parameters (temperature, windspeed, wind direction, sea-level pressure, and read timestamps), persistent manual coordinate updates via isolated text-field cards, and layout structural rearrangement across portrait and landscape viewports.

## 3. Architecture and Design
The complete project follows a decoupled architecture, mapping distinct structural contexts into completely isolated directories and package spaces:

GreetingProcessorProject/
├── annotations/
│   └── Extratc.kt
│   └── Greeting.kt
├── processor/
│   └── ExtractProcessor.kt
│   └── GreetingProcessor.kt
└── app/
    └── DataProcessor.kt
    └── MyClass.kt
    └── Main.kt

CoolWeatherApp/
└── src/
    ├── MainActivity.kt
    │   ├── data/
    │   ├── WeatherApiClient.kt
    │   └── WeatherData.kt
    ├── viewmodel/
    │   └── WeatherViewModel.kt
    └── ui/
        ├── WeatherScreen.kt
        ├── CoordinatesCard.kt
        ├── WeatherCard.kt
        └── WeatherRow.kt

## 4. Implementation
### 4.1. Annotation Processor Module & Generation Algorithms
The metaprogramming implementation establishes an explicit build-time processing sequence.
- Annotations Module: Declares the targeted metadata type via a concise class structure compiled with a source retention policy
- Processor Module: Uses Google’s @AutoService to register the processor seamlessly into the JVM compiler path. The core generation logic parses intercepted ExecutableElement functions, organizing them by their enclosing parent classes. It then leverages KotlinPoet to programmatically build equivalent decorator classes using constructor injection. For each annotated class, a wrapper is emitted to kapt.kotlin.generated that interceptively runs the personalized logging statement before safely calling the baseline function reference.

### 4.2. Weather Application Component Isolation
To maximize reuse and comply with single responsibility principles, the UI surface was broken out from WeatherScreen.kt into explicit sub-components:
- WeatherUIState.kt (State Model): To maintain maximum usability while typing coordinates, the state parameters are tracked as pure floats, while the interactive View layers manage incoming inputs natively before parsing
- CoordinatesCard.kt (User Input View): Encapsulates the coordinate OutlinedTextField boxes, applying specialized shape structures and width boundaries to ensure visual symmetry on tablets.
- WeatherCard.kt (Data Matrix Grid): Implements WeatherCardCustom, binding weather indicators with faint alpha horizontal lines (HorizontalDivider).

## 5. Testing and Validation
Validation was performed across multiple runtime contexts to verify that execution limits and interface adaptations behave exactly as required:
- Run compile-stage on @Greeting("Test")
- Call getWeather() with valid floats
- Shift screen layout dynamically (90º)
- Switch device settings to pt-PT locale

## 6. Usage Instructions
### 6.1. Environment Configuration Requirements
- Development Environments: IntelliJ IDEA (For Module 1 Processing) & Android Studio Jellyfish/Ladybug (For Module 2 Weather App).
- Build System Automation: Gradle Kotlin DSL (.build.gradle.kts) targeting toolchain JDK 23.
- Android Target Specifications: Minimum SDK: 26 (Android 8.0), Target SDK: 34 (Android 14).

### 6.2. Installation and Build Procedures
- Import the root repository directory into the targeted development environment.
- For the processor system, navigate to Settings->Compiler->Annotation Processors and explicitly check the "Enable annotation processing" toggle.
- Sync the project Gradle files to pull down third-party dependencies (Ktor, KotlinPoet, AutoService).
- Run the annotation verification by initiating an application build task.
- Deploy the mobile application to an active Android Virtual Device (AVD) or an attached hardware developer device. Tap the update button to query live weather metrics across target locations.

# Autonomous Software Engineering Sections - only for [AC OK , AI OK] sections
## 7. Prompting Strategy
AI tools were used as cooperative engineering assistants throughout the development cycle. Prompts were designed to solve specific architecture questions:
Initial Prompt Example: "Create a custom annotation processor in Kotlin using kapt that generates wrapper classes using KotlinPoet."
Iterative Refinement Example: "The Jetpack Compose text field resets to 0.0 whenever a user tries to type a negative coordinates sign because toFloatOrNull fails. Give me an updated state hoisting pattern that isolates partial user typing strings."

## 8. Autonomous Agent Workflow
Automated engines contributed directly across various development stages:
- Layout Design: Structuring the basic look of the responsive multi-orientation layout blocks.
- Boilerplate Production: Producing standard setup code for multi-module Gradle setups and generating Ktor client initialization templates.
- Debugging Automation: Finding optimization bugs within compile-time code-generation loops.

## 9. Verification of AI - Generated Artifacts
All AI-generated code was manually reviewed and tested before integration.
AI outputs were treated as raw source recommendations and subjected to thorough validation:
- Component Code Inspection: Reviewing code formatting and structure by hand to ensure that every visual composable maintained a single responsibility.

## 10. Human vs AI Contribution
Human-Developed:
Project planning and interpretation of assignment requirements
Final integration of all modules
Manual debugging and testing
UI adjustments and layout refinements
Theme configuration corrections
State preservation logic

AI-Assisted:
Debugging suggestions
Architecture recommendations
Documentation support

## 11. Ethical and Responsible Use
AI tools were used strictly as development assistants and learning aids.

Risks encountered:
Occasionally incorrect Android lifecycle advice
Some generated code not aligned with assignment requirements
Overly generic implementations requiring adaptation

Mitigations:
Manual validation of all outputs
Testing every generated artifact
Rejecting unsuitable AI suggestions

# Development Process
## 12. Version Control and Commit History
Git was used throughout development to maintain project history.
Commit strategy included:
Initial project setup
Annotation Processor implementation
Weather API integration
Updated API
Final testing and cleanup

Commit history reflects incremental progress rather than a single final upload.

## 13. Difficulties and Lessons Learned
Main Challenges:
- Volatile Input Processing
- Metaprogramming Isolation

## 14. Future Improvements
Potential enhancements include:
- Advanced Architecture Transition
- Persistent Local Database Caching

## 15. AI Usage Disclosure ( Mandatory )
The following AI tools were used during development:
ChatGPT, DeepSeek, Claude, Gemini, Antigravity
- Android debugging help
- Documentation assistance
- Architecture explanations
- Alternative debugging suggestions
- Code comparison and validation
- AI-assisted Android development environment
- Build/deploy workflow assistance
- Guided code generation
