package com.ketchupzzz.isaom.models

data class PrivacyPolicy(
    val lastUpdated: String,
    val interpretation: Interpretation,
    val definitions: Definitions,
    val personalData: PersonalData,
    val usageData: UsageData,
    val informationCollected: InformationCollected,
    val personalDataUsage: PersonalDataUsage,
    val dataRetention: DataRetention,
    val dataTransfer: DataTransfer,
    val dataDeletion: DataDeletion,
    val dataDisclosure: DataDisclosure,
    val security: Security,
    val externalLinks: List<String>,
    val policyChanges: PolicyChanges,
    val contactInfo: ContactInfo
)

data class Interpretation(
    val wordsDefined: List<String>
)

data class Definitions(
    val account: String,
    val application: String,
    val company: String,
    val country: String,
    val device: String,
    val personalData: String,
    val service: String,
    val serviceProvider: String,
    val usageData: String,
    val you: String
)

data class PersonalData(
    val emailAddress: String,
    val firstName: String,
    val lastName: String,
    val lrn: String
)

data class UsageData(
    val ipAddress: String,
    val browserType: String,
    val browserVersion: String,
    val pagesVisited: List<String>,
    val timeSpent: String,
    val uniqueDeviceID: String,
    val diagnosticData: String
)

data class InformationCollected(
    val mobileDeviceID: String,
    val deviceType: String,
    val mobileOS: String,
    val internetBrowserType: String,
    val diagnosticData: String,
    val cameraPermission: Boolean
)

data class PersonalDataUsage(
    val serviceMaintenance: Boolean,
    val manageAccount: Boolean,
    val contact: Boolean,
    val manageRequests: Boolean,
    val promotionalOffers: Boolean,
    val analysis: Boolean
)

data class DataRetention(
    val personalDataDuration: String,
    val usageDataDuration: String
)

data class DataTransfer(
    val transferConsent: Boolean,
    val location: String
)

data class DataDeletion(
    val deletionRequest: Boolean,
    val accountSettings: Boolean
)

data class DataDisclosure(
    val businessTransactions: Boolean,
    val legalRequirements: List<String>
)

data class Security(
    val encryption: Boolean,
    val limitations: String
)

data class PolicyChanges(
    val notificationMethod: String,
    val effectiveDate: String
)

data class ContactInfo(
    val email: String
)
