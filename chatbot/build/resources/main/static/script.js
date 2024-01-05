// script.js
var colorThief = new ColorThief();
var img = document.getElementById('responseImage');
img.onload = function () {
    var color = colorThief.getColor(img);
    img.style.boxShadow = "0 0 25px rgb(" + color + ")";
};

// Models data
// script.js

// This function should be called when you receive the response from your AI model
function handleResponse() {
    // Get the 'response' element from the HTML
    var responseElement = document.getElementById('response');

    // Get the text content of the 'response' element
    var response = responseElement.textContent;

    // Now you can use 'response' in your JavaScript code
}

var textModels = [
    "gpt-3.5-turbo",
    "gemini-pro",
    "gemini-pro-vision",
    "llama-2-70b-chat",
    "llama-2-13b-chat",
    "llama-2-7b-chat",
    "code-llama-34b",
    "mistral-7b",
    "mixtral-8x7b"
];

var imageModels = [
    { "id": "sdxl", "name": "SDXL" },
    { "id": "latent-consistency-model", "name": "Latent Consistency Model" },
    { "id": "kandinsky-2.2", "name": "Kandinsky 2.2" },
    { "id": "kandinsky-2", "name": "Kandinsky 2" },
    { "id": "stable-diffusion-2.1", "name": "Stable Diffusion 2.1" },
    { "id": "stable-diffusion-1.5", "name": "Stable Diffusion 1.5" },
    { "id": "deepfloyd-if", "name": "DeepFloyd-IF" },
    { "id": "material-diffusion", "name": "Material Diffusion" }
];

// Function to populate model dropdown based on the selected type
function populateModels(selectedType) {
    var modelDropdown = $("#model");
    modelDropdown.empty(); // Clear existing options

    if (selectedType === "image") {
        // Populate models based on the selected type
        imageModels.forEach(function (model) {
            modelDropdown.append($("<option />").val(model.id).text(model.name));
        });
    } else {
        // Populate text models for chat type
        textModels.forEach(function (model) {
            modelDropdown.append($("<option />").val(model).text(model));
        });
    }

    $("#model-group").show(); // Always show the model dropdown
}

// Initial population of models based on the default type
populateModels($("#type").val());

// Handle type change event
$("#type").change(function () {
    var selectedType = $(this).val();
    populateModels(selectedType);
});

$("#form").submit(function (event) {
    event.preventDefault();
    $("#loading").show();
    var prompt = $("#prompt").val();
    var type = $("#type").val();
    var model = $("#model").val(); // Get the selected model
    var url = "http://localhost:8080/" + type;

    // ... (previous code)

    $.ajax({
        url: url,
        type: "post",
        data: {
            prompt: prompt,
            model: model
        },
        success: function (response) {
            $("#loading").hide();

            if (type === "chat") {
                // Display words one by one
                displayWords(response);
                $("#response-code").hide();
                $("#copy-button").hide();
                $("#responseImage").hide();
            } else if (type === "image") {
                // Parse the JSON response
                var imageData = JSON.parse(response);

                // Set the image source based on the URL in the API response
                $("#responseImage").attr("src", imageData.data[0].url).show();

                // Hide other elements
                $("#response").text("");
                $("#response-code").hide();
                $("#copy-button").hide();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus, errorThrown);
            $("#loading").hide();
        }
    });

    // ... (remaining code)
});

function displayWords(response) {
    var words = response.split(" ");
    var index = 0;

    function displayNextWord() {
        if (index < words.length) {
            $("#response").text(words.slice(0, index + 1).join(" "));
            index++;
            setTimeout(displayNextWord, 10); // Adjust the delay between words (in milliseconds)
        }
    }

    displayNextWord();
}
// This function should be called when you receive the response from your AI model
function handleResponse(response) {
    // 'response' is the response from your AI model

    // Get the 'response' div from the HTML
    var responseDiv = document.getElementById('response');

    // Clear the 'response' div
    responseDiv.innerHTML = '';

    // Check if the response is a code block
    if (response.startsWith('```') && response.endsWith('```')) {
        // Create a new pre element for the code block
        var pre = document.createElement('pre');

        // Set the innerHTML of the pre element to the response
        // We remove the starting and ending ``` from the response
        pre.innerHTML = response.slice(3, -3);

        // Create a new div element for the code block
        var div = document.createElement('div');

        // Add the 'code' class to the div
        div.classList.add('code');

        // Append the pre element to the div
        div.appendChild(pre);

        // Append the div to the 'response' div
        responseDiv.appendChild(div);
    } else {
        // Insert 'response' into the HTML as you did before
        responseDiv.textContent = response;
    }
}



function copyCodeToClipboard() {
    const codeContainer = document.getElementById('response-code');
    const range = document.createRange();
    range.selectNode(codeContainer);
    window.getSelection().removeAllRanges();
    window.getSelection().addRange(range);
    document.execCommand('copy');
    window.getSelection().removeAllRanges();
    alert('Code copied to clipboard!');
}