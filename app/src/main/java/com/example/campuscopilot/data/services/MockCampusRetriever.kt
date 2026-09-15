package com.example.campuscopilot.data.services

import com.example.campuscopilot.data.models.CampusCategory
import com.example.campuscopilot.data.models.CampusDocument
import com.example.campuscopilot.domain.services.CampusRetriever
import kotlinx.coroutines.delay

class MockCampusRetriever : CampusRetriever {

    val knowledgeBase = listOf(
        // FACILITIES
        CampusDocument(
            id = "fac_1",
            title = "University Recreation Center & Gym",
            category = CampusCategory.FACILITIES,
            content = "The University Recreation Center features a state-of-the-art gym, indoor running track, Olympic swimming pool, and basketball courts. Open daily from 6:00 AM to 10:00 PM for all registered students.",
            tags = listOf("gym", "fitness", "sports", "recreation", "pool", "workout")
        ),
        CampusDocument(
            id = "fac_2",
            title = "Student Innovation & Robotics Lab",
            category = CampusCategory.FACILITIES,
            content = "Located in Engineering Block B, Room 302. Equips 3D printers, laser cutters, PCB etching equipment, and High-Performance Workstations. Access available via swipe card for STEM students.",
            tags = listOf("lab", "robotics", "3d printing", "engineering", "makerspace")
        ),
        CampusDocument(
            id = "fac_3",
            title = "Central Health Center & Clinic",
            category = CampusCategory.FACILITIES,
            content = "Free primary healthcare, first-aid, emergency response, and confidential counseling for enrolled students. Located next to Student Union, open Mon-Fri 8:00 AM - 6:00 PM.",
            tags = listOf("health", "clinic", "doctor", "medicine", "counseling", "therapy")
        ),
        CampusDocument(
            id = "fac_4",
            title = "Auditorium & Performance Hall",
            category = CampusCategory.FACILITIES,
            content = "1,200 seat theater hosting university convocations, guest lectures, cultural festivals, and drama productions. Main Campus Block A.",
            tags = listOf("auditorium", "events", "theater", "music", "hall")
        ),
        CampusDocument(
            id = "fac_5",
            title = "Quiet Study Pods & Media Center",
            category = CampusCategory.FACILITIES,
            content = "Soundproof study pods reservable online via student portal. Located on the 3rd floor of Central Library.",
            tags = listOf("study", "quiet", "pods", "library", "space")
        ),

        // CLUBS
        CampusDocument(
            id = "club_1",
            title = "Coding Club",
            category = CampusCategory.CLUBS,
            content = "The Coding Club helps students explore programming, competitive coding, software development and technical projects. Weekly hackathons and workshops every Wednesday at 5 PM in CS Lab 1.",
            tags = listOf("coding", "programming", "software", "tech", "python", "hackathon", "clubs")
        ),
        CampusDocument(
            id = "club_2",
            title = "AI & Data Science Society",
            category = CampusCategory.CLUBS,
            content = "Focuses on Machine Learning, Deep Learning, Large Language Models, and Kaggle competitions. Open to all majors. Meets Thursdays at 6 PM.",
            tags = listOf("ai", "machine learning", "data science", "tech", "clubs")
        ),
        CampusDocument(
            id = "club_3",
            title = "Debate & Public Speaking Society",
            category = CampusCategory.CLUBS,
            content = "Enhance public speaking, parliamentary debate, and critical thinking skills. Organizes annual inter-university Model UN. Meets Tuesdays at 5 PM.",
            tags = listOf("debate", "mun", "speaking", "clubs", "arts")
        ),
        CampusDocument(
            id = "club_4",
            title = "Photography & Digital Arts Club",
            category = CampusCategory.CLUBS,
            content = "Conducts photo walks, digital design workshops, and semester art exhibitions. Studio equipment available for loan to members.",
            tags = listOf("photography", "art", "design", "creative", "clubs")
        ),
        CampusDocument(
            id = "club_5",
            title = "Entrepreneurship & Startup Cell",
            category = CampusCategory.CLUBS,
            content = "Connects student founders with mentors, angel investors, and incubation grants. Organizes annual Pitch Fest and Founder Talks.",
            tags = listOf("startup", "business", "entrepreneurship", "pitch", "clubs")
        ),

        // SPORTS
        CampusDocument(
            id = "sports_1",
            title = "University Sports Complex & Athletics",
            category = CampusCategory.SPORTS,
            content = "Facilities include floodlit football turf, cricket nets, tennis courts, badminton courts, and table tennis. Open 6:00 AM - 9:00 PM.",
            tags = listOf("sports", "football", "cricket", "tennis", "badminton", "games")
        ),
        CampusDocument(
            id = "sports_2",
            title = "Inter-College Basketball League",
            category = CampusCategory.SPORTS,
            content = "Semester-long tournament featuring men's and women's varsity teams. Open trials conducted at the start of Fall and Spring terms.",
            tags = listOf("basketball", "league", "sports", "varsity")
        ),
        CampusDocument(
            id = "sports_3",
            title = "Esports & Gaming Guild",
            category = CampusCategory.SPORTS,
            content = "Competitive gaming teams for Valorant, Rocket League, Chess, and League of Legends. Equipped with high-refresh rate gaming rigs in Student Center.",
            tags = listOf("esports", "gaming", "chess", "sports")
        ),

        // ACADEMICS
        CampusDocument(
            id = "acad_1",
            title = "Academic Advising & Registrar",
            category = CampusCategory.ACADEMICS,
            content = "Assists with course registration, credit transfers, degree audits, and drop/add deadlines. Admin Building 1st Floor.",
            tags = listOf("academics", "registrar", "courses", "registration", "advising", "credits")
        ),
        CampusDocument(
            id = "acad_2",
            title = "Peer Tutoring & Learning Center",
            category = CampusCategory.ACADEMICS,
            content = "Free 1-on-1 tutoring in Mathematics, Physics, Computer Science, and Chemistry. Walk-in and appointment slots daily 10 AM - 6 PM.",
            tags = listOf("tutoring", "academics", "help", "math", "python", "physics")
        ),
        CampusDocument(
            id = "acad_3",
            title = "Undergraduate Research Grants",
            category = CampusCategory.ACADEMICS,
            content = "Funding up to $2,000 for student-led research projects under faculty supervision. Applications open every semester in September and January.",
            tags = listOf("research", "grant", "academics", "funding")
        ),

        // EVENTS
        CampusDocument(
            id = "event_1",
            title = "Annual Tech Fest — HackCampus 2026",
            category = CampusCategory.EVENTS,
            content = "36-hour hackathon with $10,000 prize pool, industry mentors, and sponsor booths. Scheduled for October 15–17 at Central Auditorium.",
            tags = listOf("events", "tech fest", "hackathon", "competition")
        ),
        CampusDocument(
            id = "event_2",
            title = "Campus Career & Internship Fair",
            category = CampusCategory.EVENTS,
            content = "Over 80 top tech, finance, and engineering companies recruiting on campus. September 28 at Sports Complex.",
            tags = listOf("events", "career", "jobs", "internship", "fair")
        ),

        // LIBRARY
        CampusDocument(
            id = "lib_1",
            title = "Central University Library",
            category = CampusCategory.LIBRARY,
            content = "Houses over 250,000 physical books, digital journal access (IEEE, ACM, JSTOR), 24/7 exam reading hall, and discussion rooms. Open 24/7 during final exam weeks.",
            tags = listOf("library", "books", "study", "journals", "timings", "reading")
        ),

        // CAFETERIA
        CampusDocument(
            id = "cafe_1",
            title = "Student Center Food Court & Cafeteria",
            category = CampusCategory.CAFETERIA,
            content = "Features fresh salad bar, pizza counter, Asian bowls, coffee bar, and vegan options. Subsidized meal plans available. Open 7:00 AM - 11:00 PM.",
            tags = listOf("cafeteria", "food", "dining", "coffee", "lunch", "meal")
        ),

        // HOSTEL
        CampusDocument(
            id = "hostel_1",
            title = "Campus Residential Halls & Hostels",
            category = CampusCategory.HOSTEL,
            content = "On-campus housing with high-speed Wi-Fi, laundry facilities, study lounges, and 24/7 security guard post. Resident Wardens available on-call.",
            tags = listOf("hostel", "dorm", "housing", "residence", "laundry")
        ),

        // TRANSPORT
        CampusDocument(
            id = "trans_1",
            title = "Campus Shuttle & Metro Connectivity",
            category = CampusCategory.TRANSPORT,
            content = "Free electric shuttle runs every 15 minutes connecting North Campus, South Campus, Metro Station, and Student Housing. Operates 6:30 AM - 11:30 PM.",
            tags = listOf("transport", "shuttle", "bus", "metro", "commute", "travel")
        )
    )

    override suspend fun retrieveRelevantDocuments(query: String): List<CampusDocument> {
        delay(400) // Simulate search latency
        val cleanQuery = query.lowercase().trim()
        val queryTokens = cleanQuery.split(" ", "?", "!", ".", ",", "-").filter { it.length > 2 }

        if (queryTokens.isEmpty()) return emptyList()

        val matches = knowledgeBase.map { doc ->
            var score = 0
            val docText = "${doc.title} ${doc.content} ${doc.tags.joinToString(" ")}".lowercase()

            for (token in queryTokens) {
                if (docText.contains(token)) score += 2
                if (doc.tags.any { it.lowercase().contains(token) }) score += 3
                if (doc.category.name.lowercase() == token) score += 4
            }
            Pair(doc, score)
        }.filter { it.second > 0 }
         .sortedByDescending { it.second }
         .map { it.first }

        return matches.take(3)
    }
}
