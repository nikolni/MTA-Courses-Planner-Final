function highlightHey() {
  const bodyText = document.body.innerHTML;
  const highlightedText = bodyText.replace(/קורס/gi, '<span style="background-color: yellow;">$&</span>');
  document.body.innerHTML = highlightedText;
}

// Run the highlight function after the DOM is fully loaded
window.addEventListener('load', highlightHey);

// Function to simulate a click event on a button
function clickButton(selector) {
  const button = document.querySelector(selector);
  if (button) {
    button.click();
    console.log('Button clicked:', selector);
  } else {
    console.error('Button not found:', selector);
  }
}

// Function to send the data to the backend
function sendDataToBackend(sourceCode) {
  fetch('http://localhost:8080/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ htmlSource: sourceCode })
  }).then(response => response.json())
      .then(data => console.log('Success:', data))
      .catch((error) => console.error('Error:', error));
}

// Function to monitor DOM changes and click the button once it's available
function monitorDOMChanges(selector, callback) {
  const observer = new MutationObserver((mutationsList, observer) => {
    for (const mutation of mutationsList) {
      if (mutation.type === 'childList' || mutation.type === 'subtree') {
        if (document.querySelector(selector)) {
          callback(selector); // Calling the clickButton function
          observer.disconnect(); // Stop observing after the button is clicked
        }
      }
    }
  });

  observer.observe(document.body, { childList: true, subtree: true });
}


// Function to capture the source code of the page
function captureSourceCode() {
  const sourceCode = document.documentElement.outerHTML;
  sendDataToBackend(sourceCode);
}

// Wait for the page to load completely
window.addEventListener('load', function() {
  // Monitor the DOM for the button and click it once available
  monitorDOMChanges('button[onclick="send_form(\'MenuCall\',\'-N,-N,-N61,-AH\',\'\');"]', clickButton);
});

// Add an event listener for the navigation event
window.addEventListener('load', function() {
  setTimeout(() => {
    captureSourceCode();
  }, 5000);  // Adjust the delay as needed to ensure the new page is fully loaded
});