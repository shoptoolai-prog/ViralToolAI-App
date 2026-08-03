package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// PHASE 10: LEVEL 9 - AI PAYMENT & FINANCE HUB
// ============================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel9AIPaymentFinanceHubView(
    userNiche: String,
    userPlatform: String,
    userName: String,
    onLevel9Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel9Data(context) }

    val initialModule = (savedData["module"] as? Number)?.toInt() ?: 1
    var currentModule by remember { mutableStateOf(initialModule) }

    // Module 1 State: Payment Methods
    var selectedMethod by remember { mutableStateOf("UPI") }

    // Module 2 State: Payment Timeline
    var selectedTimeline by remember { mutableStateOf("50/50 Advance & Final") }

    // Module 3 State: Invoice Builder
    var invNum by remember { mutableStateOf((savedData["invNum"] as? String) ?: "INV-2026-001") }
    var invBrand by remember { mutableStateOf((savedData["brandName"] as? String) ?: "Boat Audio") }
    var invCreator by remember { mutableStateOf(if (userName.isBlank()) "Creator Pro" else userName) }
    var invService by remember { mutableStateOf("1x Instagram Reel + 2x Stories Promotion") }
    var invAmount by remember { mutableStateOf((savedData["amount"] as? String) ?: "₹15,000") }
    var invDate by remember { mutableStateOf("01-Aug-2026") }
    var invTerms by remember { mutableStateOf("Net 7 Days Upon Invoice") }
    var isInvoiceSaved by remember { mutableStateOf(false) }

    // Module 4 State: Late Payment Guide
    var selectedReminderStage by remember { mutableStateOf("Gentle 1st Reminder") }
    var copiedScript by remember { mutableStateOf(false) }

    // Module 5 State: Fake Payment Detector
    var selectedScamType by remember { mutableStateOf("Fake UTR / Screenshot") }

    // Module 6 State: Finance Management
    var incomeAccountSeparated by remember { mutableStateOf(true) }

    // Module 7 State: Budget Planner
    var monthlyGoalInput by remember { mutableStateOf("50000") }

    // Module 9 State: Payment Safety Checklist
    var chkAgreement by remember { mutableStateOf(true) }
    var chkInvoiceSent by remember { mutableStateOf(true) }
    var chkMethodVerified by remember { mutableStateOf(true) }
    var chkAmountConfirmed by remember { mutableStateOf(true) }
    var chkDeliveryConfirmed by remember { mutableStateOf(true) }
    var chkProofSaved by remember { mutableStateOf(true) }

    fun persistState(completed: Boolean = false) {
        CreatorAcademyPrefs.saveBrandCollabLevel9State(
            context = context,
            step = currentModule,
            module = currentModule,
            invNum = invNum,
            brandName = invBrand,
            amount = invAmount,
            isCompleted = completed
        )
    }

    // AI Mentor Messages per Module
    val mentorMessages = remember(currentModule) {
        when (currentModule) {
            1 -> "Deal close ho gayi. Ab sabse important step hai... Safe payment lena aur professional finance manage karna."
            2 -> "Payment Timeline: Kabhi bhi 100% video posting ke baad bina advance mat karo. 50% advance is standard."
            3 -> "Invoice Builder: Professional PDF/Text Invoice bhejne se brand accounting team instantly payment process karti hai."
            4 -> "Late Payment Guide: Payment delay hone par gussa hone ki jagah professional reminder templates use karo."
            5 -> "Fake Payment Detector: Fake UTR aur fake bank screenshots se savdhan! Bank app me live balance verify karo."
            6 -> "Finance Management: Personal account aur creator business account separate rakho clear accounting ke liye."
            7 -> "Budget Planner: Har brand payout me se 30% savings & 20% equipment upgrade ke liye allocate karo."
            8 -> "Tax Basics: Income records rakho, GST thresholds samjho aur seasonal tax professional se consultation lo."
            9 -> "Payment Safety Checklist: Payout release hone se pehle ye 6 critical steps verify karo."
            10 -> "Today's Mission: Generate your official invoice & claim the Finance Ready Creator Badge!"
            else -> "Always manage creator earnings like a real media business!"
        }
    }

    // Pulse animation for Progress Ring (93%)
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim_l9")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_l9"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B132B),
                        Color(0xFF1C2541),
                        Color(0xFF090D16)
                    )
                )
            )
            .padding(16.dp)
    ) {
        // ================= HEADER & PROGRESS (93%) =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x221C2541))
                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFD700))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("LEVEL 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Payment & Finance Hub", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("Get Paid Like A Professional Creator • Phase 10", fontSize = 11.5.sp, color = Color(0xFFFFD700).copy(alpha = 0.9f))
                }

                // Progress Ring (93%)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                        .border(2.5.dp, Color(0xFFFFD700).copy(alpha = pulseGlow), CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("93%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        Text("Progress", fontSize = 7.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= AI MENTOR BANNER =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0x33FFD700),
                            Color(0x151C2541)
                        )
                    )
                )
                .border(1.dp, Color(0x44FFD700), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700))
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💰", fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("AI Finance Coach", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text(
                        text = mentorMessages,
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= MODULE TABS =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val modules = listOf(
                1 to "1. Payment Methods",
                2 to "2. Payment Timeline",
                3 to "3. Invoice Builder",
                4 to "4. Late Payments",
                5 to "5. Fake Payment Detector",
                6 to "6. Finance Tracking",
                7 to "7. Budget Planner",
                8 to "8. Tax Basics",
                9 to "9. Safety Checklist",
                10 to "10. Mission & Badge"
            )

            modules.forEach { (mNum, mName) ->
                val isSel = currentModule == mNum
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                        .clickable {
                            currentModule = mNum
                            persistState()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = mName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.Black else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= MODULE CONTENT AREA =================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            when (currentModule) {
                // MODULE 1: PAYMENT METHODS
                1 -> {
                    Text("Module 1: Creator Payment Methods", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select a payment channel to analyze Pros, Cons & Best Use Cases:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val methods = listOf("UPI", "Bank Transfer", "PayPal", "Wise", "Stripe", "Other")

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        methods.forEach { m ->
                            val isSel = selectedMethod == m
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable { selectedMethod = m }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(m, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val (pros, cons, bestFor) = when (selectedMethod) {
                        "UPI" -> Triple(
                            "Instant transfer, zero transaction fee for domestic Indian payouts.",
                            "Daily transaction limits (usually ₹1 Lakh), domestic India only.",
                            "Quick domestic Indian brand deals up to ₹50,000."
                        )
                        "Bank Transfer" -> Triple(
                            "NEFT/IMPS/RTGS handles large payouts without low caps, formal bank records.",
                            "Can take 24-48 hours for NEFT clearance during holidays.",
                            "Official agency campaigns & corporate Indian brand invoices above ₹20,000."
                        )
                        "PayPal" -> Triple(
                            "Global reach, supports 100+ currencies, automated buyer/seller records.",
                            "High currency conversion markup (3.5%-5%) + international fee.",
                            "International brands in USA, Europe & Asia paying small creators."
                        )
                        "Wise" -> Triple(
                            "Mid-market real exchange rates, significantly lower conversion fees than PayPal.",
                            "Requires account setup & SWIFT code verification.",
                            "International brand sponsorships & ongoing affiliate payouts."
                        )
                        "Stripe" -> Triple(
                            "Direct credit card invoicing & recurring subscription payments.",
                            "2.9% + $0.30 transaction fee per transaction.",
                            "Selling digital products, course pre-orders or memberships."
                        )
                        else -> Triple(
                            "Escrow or Agency payment gateways provide structured safety.",
                            "Additional platform fee commission.",
                            "First-time brand deals with unfamiliar international agencies."
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text("💳 $selectedMethod Overview", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("✅ Pros: $pros", fontSize = 11.5.sp, color = Color(0xFF00FF88))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("❌ Cons: $cons", fontSize = 11.5.sp, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🎯 Best Use Case: $bestFor", fontSize = 11.5.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 2: PAYMENT TIMELINE
                2 -> {
                    Text("Module 2: Payment Timeline & Safest Structure", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Compare payment schedules and risk exposure:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val timelines = listOf("100% Advance", "50/50 Advance & Final", "Milestone Payouts", "Net 30 Days Post Posting")

                    timelines.forEach { tl ->
                        val isSel = selectedTimeline == tl
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable { selectedTimeline = tl }
                                .padding(12.dp)
                        ) {
                            Text(if (tl == "50/50 Advance & Final") "⭐ $tl (Safest Standard)" else "⏳ $tl", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val timelineDesc = when (selectedTimeline) {
                        "100% Advance" -> "100% money in bank before video production. Excellent for high production cost projects."
                        "50/50 Advance & Final" -> "GOLD STANDARD: 50% advance before video shoot + 50% remaining balance upon draft approval or posting date."
                        "Milestone Payouts" -> "30% Script approval, 40% Video Draft approval, 30% Live posting date. Best for long campaigns."
                        else -> "HIGH RISK: You post content today, but brand pays after 30-60 days. Requires formal contract with interest penalties for late payment."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFF00FF88), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Text(timelineDesc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                    }
                }

                // MODULE 3: INVOICE BUILDER
                3 -> {
                    Text("Module 3: Creator Invoice Builder", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Fill details to construct a professional invoice:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = invNum,
                        onValueChange = { invNum = it; isInvoiceSaved = false },
                        label = { Text("Invoice Number", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFFD700), unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = invBrand,
                            onValueChange = { invBrand = it; isInvoiceSaved = false },
                            label = { Text("Brand Name", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFFD700), unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        OutlinedTextField(
                            value = invAmount,
                            onValueChange = { invAmount = it; isInvoiceSaved = false },
                            label = { Text("Amount (₹)", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f)) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFFD700), unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = invService,
                        onValueChange = { invService = it; isInvoiceSaved = false },
                        label = { Text("Service Deliverables", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFFD700), unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // PREVIEW CARD
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🧾 TAX INVOICE", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Text(invNum, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Billed To: $invBrand", fontSize = 11.5.sp, color = Color.White)
                            Text("Billed By: $invCreator", fontSize = 11.5.sp, color = Color.White)
                            Text("Date: $invDate • Terms: $invTerms", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.15f)))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Service: $invService", fontSize = 11.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Total Payable: $invAmount", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            isInvoiceSaved = true
                            persistState()
                            Toast.makeText(context, "Invoice Saved & Formatted Successfully! 📄", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isInvoiceSaved) "✅ Invoice Saved To History" else "Save & Generate Invoice 📄", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                // MODULE 4: LATE PAYMENT GUIDE
                4 -> {
                    Text("Module 4: Professional Late Payment Reminders", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select reminder stage to copy polite escalation scripts:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val stages = listOf("Gentle 1st Reminder", "Formal 2nd Reminder", "Final Notice Escalation")

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        stages.forEach { stg ->
                            val isSel = selectedReminderStage == stg
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        selectedReminderStage = stg
                                        copiedScript = false
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stg, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val reminderScript = when (selectedReminderStage) {
                        "Gentle 1st Reminder" -> "Hi $invBrand team! Hope you are having a great week. Just sharing a quick follow-up on Invoice $invNum ($invAmount) due on $invDate. Please let me know if you need any additional details from my end!"
                        "Formal 2nd Reminder" -> "Dear $invBrand Finance Team, Following up on Invoice $invNum ($invAmount) which is now 7 days overdue. Could you please confirm the payment clearance status or UTR reference number?"
                        else -> "Attention $invBrand Campaign Lead, Invoice $invNum ($invAmount) remains unpaid past the agreed terms. Please process payment within 48 hours to avoid campaign usage pause as per Clause 4 of our signed agreement."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("💬 Script Template:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(reminderScript, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Reminder Script", reminderScript)
                            clipboard.setPrimaryClip(clip)
                            copiedScript = true
                            Toast.makeText(context, "Reminder script copied to clipboard! 📋", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (copiedScript) "✅ Copied To Clipboard!" else "Copy Reminder Text 📋", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                // MODULE 5: FAKE PAYMENT DETECTOR
                5 -> {
                    Text("Module 5: Fake Payment Scam Detector", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Learn how scammers simulate payments & how to verify:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val scamTypes = listOf("Fake UTR / Screenshot", "Fake Payment Email", "Scam Link / Fake QR", "Overpayment Scam")

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        scamTypes.forEach { st ->
                            val isSel = selectedScamType == st
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) Color(0xFFFF4D4D) else Color(0x22FFFFFF))
                                    .clickable { selectedScamType = st }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("🚨 $st", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val (scamHow, verifyRule) = when (selectedScamType) {
                        "Fake UTR / Screenshot" -> Pair(
                            "Scammer sends photoshopped Google Pay / Paytm screenshot with fake 12-digit UTR.",
                            "NEVER trust screenshots. Open your mobile bank app directly and check credited balance!"
                        )
                        "Fake Payment Email" -> Pair(
                            "Fake email looking like PayPal/Wise saying 'Payment received but held until you send video link'.",
                            "Check official sender email domain (@paypal.com vs @gmail.com) & log in to official app directly."
                        )
                        "Scam Link / Fake QR" -> Pair(
                            "Scammer sends QR code saying 'Scan this QR code to receive your advance payment'.",
                            "GOLDEN RULE: You NEVER enter UPI PIN to receive money. Entering PIN ALWAYS deducts money!"
                        )
                        else -> Pair(
                            "Scammer accidentally 'sends ₹50,000 instead of ₹15,000' and demands ₹35,000 refund back immediately.",
                            "Wait until funds actually settle in bank account. Inform bank official support if suspicious deposit occurs."
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x33FF4D4D))
                            .border(1.dp, Color(0xFFFF4D4D), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("⚠️ How Scam Works:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(scamHow, fontSize = 11.5.sp, color = Color.White, lineHeight = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("🛡️ How To Verify Safely:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                            Text(verifyRule, fontSize = 11.5.sp, color = Color.White, lineHeight = 15.sp)
                        }
                    }
                }

                // MODULE 6: FINANCE MANAGEMENT
                6 -> {
                    Text("Module 6: Creator Business Finance Management", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Treating creator income like a real media company:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val principles = listOf(
                        "1. Separate Creator Account: Use a dedicated bank account for brand payouts.",
                        "2. Track Every Rupee: Log date, brand name, invoice ID & gross amount.",
                        "3. Deduct Legitimate Expenses: Camera gear, editing software, travel & studio lighting.",
                        "4. Monthly Profit Review: Compare gross revenue vs net creator salary."
                    )

                    principles.forEach { pr ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221C2541))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(pr, fontSize = 11.5.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 7: BUDGET PLANNER
                7 -> {
                    Text("Module 7: Creator Income Budget Planner", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Enter monthly income target to calculate ideal financial allocation:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = monthlyGoalInput,
                        onValueChange = { monthlyGoalInput = it },
                        label = { Text("Monthly Revenue Target (₹)", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFFD700), unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    val target = monthlyGoalInput.toDoubleOrNull() ?: 50000.0
                    val savings = (target * 0.30).toInt()
                    val equipment = (target * 0.20).toInt()
                    val learning = (target * 0.10).toInt()
                    val business = (target * 0.40).toInt()

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text("📊 Smart Allocation (For ₹${target.toInt()} Target):", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("💼 Business Operations & Salary (40%): ₹$business", fontSize = 11.5.sp, color = Color.White)
                            Text("🛡️ Creator Emergency Savings (30%): ₹$savings", fontSize = 11.5.sp, color = Color(0xFF00FF88))
                            Text("🎥 Camera & Gear Upgrades (20%): ₹$equipment", fontSize = 11.5.sp, color = Color.White)
                            Text("📚 Courses & Skill Learning (10%): ₹$learning", fontSize = 11.5.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 8: TAX BASICS
                8 -> {
                    Text("Module 8: Educational Tax & Accounting Overview", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Basic principles on income documentation & professional guidance:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val taxTopics = listOf(
                        "📄 Income Documentation: Keep all brand invoices & bank receipts organized for annual review.",
                        "🧾 Invoice Serial Numbers: Sequential numbering ensures clean bookkeeping.",
                        "🔍 Threshold Awareness: In India, GST registration & tax filing thresholds apply based on annual turnover.",
                        "👨‍💼 Professional Consultation: Always work with a certified CA or accountant for official filings."
                    )

                    taxTopics.forEach { top ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221C2541))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Text(top, fontSize = 11.5.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // MANDATORY LEGAL DISCLAIMER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x33FF4D4D))
                            .padding(10.dp)
                    ) {
                        Text(
                            "⚠️ DISCLAIMER: This section is for general educational awareness only and does not constitute formal tax, legal or financial advice. Tax laws vary by jurisdiction. Always consult a qualified Chartered Accountant or financial advisor for specific tax matters.",
                            fontSize = 9.5.sp,
                            color = Color.White,
                            lineHeight = 13.sp
                        )
                    }
                }

                // MODULE 9: PAYMENT SAFETY CHECKLIST
                9 -> {
                    Text("Module 9: Payment Safety Checklist", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Ensure all 6 payment safety checkpoints are checked:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val checkItems = listOf(
                        "1. Signed Written Agreement / Email Confirmation" to chkAgreement,
                        "2. Professional Invoice Sent To Finance Team" to chkInvoiceSent,
                        "3. Payment Channel Verified (Official Bank / UPI)" to chkMethodVerified,
                        "4. Exact Payout Amount Confirmed in Writing" to chkAmountConfirmed,
                        "5. Video Deliverable Link Delivered & Confirmed" to chkDeliveryConfirmed,
                        "6. Bank Account Credit Statement Verified Live" to chkProofSaved
                    )

                    checkItems.forEachIndexed { idx, (label, state) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x221C2541))
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = state,
                                onCheckedChange = {
                                    when (idx) {
                                        0 -> chkAgreement = it
                                        1 -> chkInvoiceSent = it
                                        2 -> chkMethodVerified = it
                                        3 -> chkAmountConfirmed = it
                                        4 -> chkDeliveryConfirmed = it
                                        5 -> chkProofSaved = it
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFFD700))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(label, fontSize = 11.5.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 10: TODAY'S MISSION & ACHIEVEMENT BADGE
                10 -> {
                    Text("Module 10: Today's Finance Mission", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Estimated Time: 10 Minutes", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text("🎯 Mission: Create Your First Professional Invoice", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("You have configured invoice details ($invNum for $invBrand). Your financial workflow is now fully calibrated for professional brand deals!", fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ACHIEVEMENT BADGE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x44FFD700),
                                        Color(0x11FFD700)
                                    )
                                )
                            )
                            .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏆 ACHIEVEMENT UNLOCKED", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Finance Ready Creator", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Reward: +800 XP • Phase 10 Complete!", fontSize = 12.sp, color = Color(0xFF00FF88))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= BOTTOM BUTTON BAR =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    if (currentModule > 1) {
                        currentModule--
                        persistState()
                    } else {
                        onBack()
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("‹ Back", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (currentModule < 10) {
                        currentModule++
                        persistState()
                    } else {
                        persistState(completed = true)
                        onLevel9Completed()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (currentModule < 10) "Continue ›" else "Finish Level 9 🎉",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
