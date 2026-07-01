# Final Project - HJC Performance
Course: LEIC
Student: Rodrigo Amaral
Date: 07/01/2026
Repository URL: https://github.com/6H0ST3010/Computacao_movel

---

## 1. Introduction
For this Final Project Assignment was to build our own mobile application.

**Purpose:** The HJC Performance application was developed to digitize and simplify the management of independent automotive workshops.
**Problem Description:** Small mechanics often rely on paper notes or non-centralized digital tools, leading to lost client history, lack of real-time status tracking, and difficulty in financial monitoring.
**Objectives:**
* Provide a secure authentication system for mechanics.
* Centralize client and vehicle data.
* Enable real-time tracking of service orders.
* Offer financial insights through data visualization (Premium feature).

## 2. System Overview
HJC Performance is a mobile solution that acts as a "pocket office" for mechanics.
**Main Features:**
* **Secure Login:** Access control via Firebase Auth.
* **Client Management:** CRUD operations for client profiles, including profile photos.
* **Active Orders Dashboard:** Management of services with statuses (Open, In Progress, Completed, Cancelled).
* **Multilingual Support:** Automatic interface translation for Portuguese, English, and French.
* **Financial Reports (Premium):** Real-time bar charts showing billing by order status.

## 3. Architecture and Design
**Architecture:** The project follows an adapted **MVVM (Model-View-ViewModel)** pattern.
* **Model:** Data classes (`User`, `Cliente`, `Pedido`) representing Firestore documents.
* **View:** Fragments using **ViewBinding** for efficient UI interaction.
* **Repository:** `UserRepository.kt` centralizes all Firebase Firestore and Auth logic, providing a clean API for the UI.
**Folder Structure:**
* `ui/auth`: Authentication activities.
* `ui/clientes`: Client management components.
* `ui/dashboard`: Service order management.
* `ui/premium`: Billing reports and charts.
* `repository`: Data access logic.
* `model`: Data structures.

## 4. Implementation
**Key Technologies:**
* **Firebase Firestore:** Used for real-time synchronization.
* **MPAndroidChart:** Library used to render the financial bar chart.
* **Coil:** Efficient image loading for client profile pictures.
* **Navigation Component:** Manages transitions between fragments with custom animations.
**Code Excerpt (Dynamic Translation):**
The app uses dynamic mapping in `PedidoAdapter` and `ServicosPremiumFragment` to ensure Firestore data (stored in a common format) is displayed in the user's local language using `context.getString(R.string...)`.

## 5. Testing and Validation
**Strategy:** Manual functional testing focused on data persistence and UI responsiveness.
**Test Cases:**
* **Edge Case - No Clients:** Verified that the "New Order" dialog blocks creation if no clients exist.
* **Multilingual:** Verified that changing system language correctly updates the Bottom Navigation and Chart labels.
* **Cancelled Orders:** Validated that "Cancelled" orders are excluded from the total billing calculation in the Premium section.
**Limitations:** Image upload is currently limited to URL-based profile pictures.

## 6. Usage Instructions
1. **Requirements:** Android Studio, Android device/emulator (API 24+).
2. **Setup:**
   * Clone the repository.
   * Add your `google-services.json` to the `app/` folder.
   * Sync Gradle.
3. **Execution:** Run the `:app` module. Use the test user mail and password (Mail: teste@gmail.com, Password: teste123) or register a new user. After that explore the app as you want, add/change clients and orders, explore the simulation button in the "Premium" tab to test the financial reports. In the settings section there is a help button. 

---

# Autonomous Software Engineering Sections

## 7. Prompting Strategy
After using a prompt the response and alterations were reviewed and in some cases changed.
The strategy involved incremental development.
* **Initial Prompts:** Focused on infrastructure (Firebase dependencies, duplicate class fixes).
* **Evolution:** Moved to functional requests ("I want to add a client", "I need to edit the status").
* **Refinement:** Specific UI/UX improvements ("Add an animation", "Translate the Bottom Nav").
* **Example:** "In the premium section, if an order is cancelled, it shouldn't count towards total billing."

## 8. Autonomous Agent Workflow
The AI agent contributed by:
* **Planning:** Suggesting how to structure the Premium feature.
* **Coding:** Generating the `UserRepository` methods and Adapter logic.
* **Debugging**
* **Optimization**

## 9. Verification of AI-Generated Artifacts
After every change ask some question about the code, about what it does and manual test the check the new features to confirm.

## 10. Human vs AI Contribution
* **Human:** Concept definition, feature prioritization, graphic design choices, folder structure oversight and testing.
* **AI:** coding, troubleshooting Gradle conflicts, and automated translation mapping.

## 11. Ethical and Responsible Use
When using AI tools it needs to have caution to understand what is being done, sometimes the code generated it's not optimal, sometimes you need to specify very cleary what you want, using examples of code for example as well.

---

# Development Process

## 12. Difficulties and Lessons Learned
* **Challenge:** Handling duplicate class errors between Firebase modules.
* **Lesson:** Importance of using the **Firebase BoM (Bill of Materials)** to manage versions automatically.

## 13. Future Improvements
In the client class add like a list with the cars that the client has.
Alert mechanics when a scheduled service date is approaching, but for that I needed to add a time schedule for the service/order. 
A simplified view for vehicle owners to track their car's repair progress.

---

## 14. AI Usage Disclosure (Mandatory)
This project utilized **Android Studio AI/Gemini** for code generation, bug fixing, and report structuring. I remain responsible for the final verification, testing, and integrity of the application.
