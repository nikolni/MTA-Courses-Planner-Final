console.log('begin');

sessionStorage.setItem("loaded", false);

function checkLoadedAndRunHandler() {
  console.log("in check loaded");
  let loaded = sessionStorage.getItem("loaded") == "true";
  if (loaded) {
    onLoadHandler();
  } else {
    // Check again after a short delay
    setTimeout(checkLoadedAndRunHandler, 100); // 100ms delay
  }
}


setTimeout(() => {
  const sourceCode1 = document.documentElement.outerHTML;
  sessionStorage.setItem("loaded", true);
  sendDataToBackend(sourceCode1);
  }, 500); 

// Function to send the data to the backend
function sendDataToBackend(sourceCode1) {
  isScriptRun = true;
  fetch('https://mta-courses-planner.df.r.appspot.com/html/is-student-registered', {
  
//fetch('http://localhost:8080/html/is-student-registered', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ htmlSource: sourceCode1 })
  }).then(response => response.json())
      .then(data => {
    if (typeof data === 'boolean') { // Check if the response is a boolean
      if (data) {
        console.log('Student is registered.');
        // Handle the case where the student is registered
      } else {
        console.log('Student is not registered.');
          // Start checking if the page is loaded
        checkLoadedAndRunHandler();
      }
    } else {
      console.error('Unexpected response type:', data);
      // Handle unexpected response types
    }
  })
      .catch((error) => console.error('Error:', error));
}


// Wait for the page to load completely
function onLoadHandler() {
  //if (!scriptHasRun) {
    //scriptHasRun = true;
  console.log('onLoadHandler');
  monitorDOMChanges('button[onclick="send_form(\'MenuCall\',\'-N,-N,-N61,-AH\',\'\');"]', clickButton);
  setTimeout(() => {
    DOMContentLoaded();
    }, 2500);  // Adjust the delay as needed to ensure the new page is fully loaded
   }



// Function to monitor DOM changes and click the button once it's available
function monitorDOMChanges(selector, callback) {
  localStorage.clear();
  const observer = new MutationObserver((mutationsList, observer) => {
    if (document.querySelector(selector)) {
      callback(selector);
      observer.disconnect(); // Stop observing after the button is clicked
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

// Function to handle year selection
function DOMContentLoaded() {
  var selectElement = document.getElementById('R1C1');
  
  if (selectElement) {
    selectElement.value = "-1";  // Select "כל השנים"
    var event = new Event('change', { bubbles: true });
    selectElement.dispatchEvent(event);

    //if (!scriptHasRun) {
        injectScript('dom-monitor.js');
    //}
  }

}


/////////////////////////////////////////////////////////////////////

// Function to inject an external script into the webpage
function injectScript(file) {
  console.log('injectScript');
  var script = document.createElement('script');
  script.src = chrome.runtime.getURL(file);
  script.onload = function() {
    this.remove(); // Remove the script element after loading
  };
  (document.head || document.documentElement).appendChild(script);
}



