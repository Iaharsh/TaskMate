# TaskMate 🚀 - Enterprise Task Management for Teams

TaskMate is a cutting-edge Android application built to streamline team collaboration through powerful project organization and task tracking. It stands out with its premium aesthetic and a deep **Role-Based Access Control (RBAC)** architecture that defines clear boundaries between Project Admins and Team Members.

---

## 🌟 Vision & Purpose
Most task managers are either too simple for teams or too complex for individual users. TaskMate bridges this gap by offering a professional, high-performance environment where project owners (Admins) can orchestrate work, and collaborators (Members) can focus on execution without administrative clutter.

---

## ✨ Comprehensive Features

### 🔐 Advanced Authentication System
- **Role-Based Login**: Integrated selection for Admin/Member roles during both registration and login to ensure correct permission mapping.
- **Data Persistence**: Sessions and user preferences are securely managed using `SharedPreferences` and `Room`.
- **Dynamic Greetings**: Personalized dashboard greetings based on the time of day and the logged-in user's name.

### 🛡️ Role-Based Access Control (RBAC)
- **Admin Capabilities**:
    - Complete Project Lifecycle Management (Create/Delete).
    - Team Management: Add members via email and remove them using the **Member Chip System**.
    - Task Orchestration: Create, Delete, and **Dynamically Reassign** tasks to any project member.
- **Member Capabilities**:
    - Read-only Project Overview.
    - Personal Task Tracking: View tasks assigned to them and track overall project progress.
    - Focused UI: Non-essential administrative buttons (Delete, Add, Reassign) are automatically hidden to reduce cognitive load.

### 📊 Project & Team Orchestration
- **Intuitive Organization**: Group tasks into high-level projects with dedicated descriptions.
- **Visual Team Mapping**: Project members are displayed as interactive Chips, providing a clear view of team composition at a glance.
- **Smart Counting**: Live task status tracking (Todo, Doing, Done) with automated count updates on the main dashboard.

### 📝 Precision Task Management
- **Detailed Attributes**: Every task supports Title, Description, Priority (Low/Medium/High), and Status.
- **Assignment Engine**: Direct task-to-member assignment during creation.
- **Interactive Lifecycle**: Smooth transitions between task states with real-time database updates.

---

## 🎨 Design & UX Excellence
- **Vibrant Aesthetic**: Custom `GradientTextView` components and curated color palettes for a premium feel.
- **Glassmorphism & Material 3**: Implementation of modern design principles including elevated cards, rounded corners, and subtle shadows.
- **Reactive UI**: The interface responds instantly to state changes, providing a "live" feel to the data.

---

## 🛠 Technical Architecture

TaskMate follows the **Clean Architecture** principles and the **MVVM** design pattern to ensure scalability and maintainability.

- **Data Layer**: 
    - **Room Database**: Optimized SQL queries for complex relationships (Projects ↔ Members ↔ Tasks).
    - **MediatorLiveData**: Used in ViewModels to calculate complex UI states from multiple data sources.
- **Domain Layer**:
    - Repository pattern to abstract data sources from the UI logic.
- **UI Layer**:
    - **Data Binding & View Binding**: Minimizes boilerplate and ensures safe interaction with layout components.
    - **Hilt (Dagger)**: Industry-standard dependency injection for modular code.

### 📦 Key Dependencies
```kotlin
// Core & Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")

// Database
implementation("androidx.room:room-runtime:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.51.1")
kapt("com.google.dagger:hilt-android-compiler:2.51.1")
```

---

## 🚀 Installation & Setup

### 1. Environment Setup
- Install [Android Studio Jellyfish](https://developer.android.com/studio).
- Ensure you have **JDK 17** configured.

### 2. Cloning the Project
```bash
git clone https://github.com/yourusername/TaskMate.git
cd TaskMate
```

### 3. Build & Run
- Sync the project with Gradle files.
- Build the project (`Build > Make Project`).
- Run on an emulator or physical device (API Level 24+ required).

---

## 🗺 Roadmap & Future Improvements
- [ ] **Cloud Sync**: Firebase integration for real-time cross-device synchronization.
- [ ] **Push Notifications**: Instant alerts when a task is assigned or reassigned.
- [ ] **Dark Mode**: Complete theme overhaul for low-light environments.
- [ ] **Analytics**: Visual charts for project completion rates.

---

## 🤝 Contribution
Contributions are welcome! If you'd like to improve TaskMate:
1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
**Developed by [Your Name]** - *Dedicated to making team management effortless.*
