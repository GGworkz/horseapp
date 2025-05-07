[HorseApp - Equine Management System](https://github.com/GGworkz)  

🐎 **A full-featured CRUD application for veterinarians and horse owners, built with Spring Boot**  

Designed to streamline veterinary management, allowing veterinarians to manage clients, horses, consultations, treatments, and billing.

---

## 🛠️ **Tech Stack & Features**  

### **Backend Framework:**  
- **Spring Boot** - Fast and robust backend development framework  
- **Spring Security** - User authentication and session management  
- **Spring Data JPA** - Simplified database interactions  
- **PostgreSQL** - Scalable and reliable relational database  
- **RESTful API Documentation** - Interactive Swagger API documentation

### **Authentication & Authorization:**  
- **User Signup & Login** - Secure authentication with role-based access  
- **Session Management** - Persistent login to avoid repeated sign-ins  
- **User Role Management** - Users can register as **Veterinarians, or Customers**  

### **API Functionalities:**  
- **User Management:**
  - Signup & login
  - Role selection (**Vet, Customer**)
  - Session handling to prevent frequent logins
- **Veterinarian Features:**
  - Accept new customers & their horses
  - Manage treatment & medication records with pricing
  - Conduct consultations including treatments & prescribed medications
- **Customer Features:**
  - Request vet acceptance for themselves and their horses
  - Retrieve itemized invoices for consultations

### **Business Entities:**
- **User** - Represents veterinarians, assistants, and customers
- **Customer** - Customers linked to a veterinarian
- **Horse** - Managed under a client’s profile
- **Product** - Includes services, treatments, and medications
- **Consultation** - Medical appointments that track treatments, medications, and costs

---

## 📌 **Planned Features**  
- 🚀 **Appointment Scheduling** - Customers can book vet consultations  
- 💳 **Payment Integration** - Support for online invoicing and payments  
- 📊 **Dashboard & Reports** - Vets can track consultations and earnings

This project aims to provide an **efficient and structured system for veterinarians** to manage their practice while giving **customers control over their horse’s healthcare**.