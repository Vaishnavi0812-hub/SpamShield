document.getElementById("classifyBtn").addEventListener("click", async () => {
    const emailText = document.getElementById("emailInput").value;
    const resultBox = document.getElementById("resultBox");
    const resultText = document.getElementById("resultText");

    if (!emailText.trim()) {
        resultText.innerHTML = "❌ Please enter email text.";
        resultBox.classList.remove("hidden");
        return;
    }

    resultText.innerHTML = "⏳ Classifying...";
    resultBox.classList.remove("hidden");

    try {
        const response = await fetch("/classify", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ email: emailText })
        });

        const data = await response.json();

        if (data.result === "spam") {
            resultText.innerHTML = `🚨 <strong style="color:#ff0000;">SPAM</strong> detected!`;
        } else {
            resultText.innerHTML = `✅ <strong style="color:#00ff00;">HAM</strong> (Safe)`;
        }

    } catch (err) {
        resultText.innerHTML = "❌ Error connecting to server.";
    }
});
