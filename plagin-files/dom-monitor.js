
setTimeout(() => {
  console.log('moved to dom-monitor');
}, 500); 

monitorDOMChanges2('a.btn.btn-primary.rounded[onclick^="SubmitForm"]', clickRefreshButton);



// Function to monitor DOM changes and click the refresh button
function monitorDOMChanges2(selector, callback) {
 
   //if (document.querySelector(selector) && !scriptHasRun) {
  if (document.querySelector(selector)) {
      console.log('Button found by MutationObserver');
      callback(selector);
    }
    else{
console.log('on monitorDOMChanges2 second time');

    }
  
}

function clickRefreshButton(selector) {
  const button = document.querySelector(selector);
  if (button) {
    button.click();
    console.log('Button clicked:', selector);

    toServer();
  } 
  else 
  {
    console.error('Button not found or already clicked:', selector);
  }
}



// Add an event listener for the navigation event
function toServer() {
  console.log('setTimeout');
  setTimeout(() => {
    captureSourceCode2();
  }, 500);  // Adjust the delay as needed to ensure the new page is fully loaded
}


// Function to capture the source code of the page
function captureSourceCode2() {
  console.log('captureSourceCode2');
  const sourceCode = document.documentElement.outerHTML;
    setTimeout(() => {
    sendDataToBackend2(sourceCode);
  }, 500); 
}

// Function to send the data to the backend
function sendDataToBackend2(sourceCode) {

  console.log('sendDataToBackend2');


  fetch('https://mta-courses-planner.df.r.appspot.com/html/upload-courses-for-all-years', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ htmlSource: sourceCode })
  }).then(response => response.json())
      .then(data => console.log('Success:', data))
      .catch((error) => console.error('Error:', error));
}

