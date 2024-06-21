
// Wait for the page to load completely
window.addEventListener('load', function() {
  // setTimeout(() => {
  //     monitorDOMChanges('button[onclick="send_form(\'MenuCall\',\'-N,-N,-N61,-AH\',\'\');"]', clickButton);
  // }, 5000);  // Adjust the delay as needed to ensure the new page is fully loaded
  // Monitor the DOM for the button and click it once available
  monitorDOMChanges('button[onclick="send_form(\'MenuCall\',\'-N,-N,-N61,-AH\',\'\');"]', clickButton);
});

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



// Add an event listener for the navigation event
window.addEventListener('load', function() {
  setTimeout(() => {
    captureSourceCode();
  }, 4000);  // Adjust the delay as needed to ensure the new page is fully loaded
});


// Function to capture the source code of the page
function captureSourceCode() {
  const sourceCode = document.documentElement.outerHTML;
  sendDataToBackend(sourceCode);
}

// Function to send the data to the backend
function sendDataToBackend(sourceCode) {
  fetch('http://localhost:8080/upload-courses', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ htmlSource: sourceCode })
  }).then(response => response.json())
      .then(data => console.log('Success:', data))
      .catch((error) => console.error('Error:', error));
}

