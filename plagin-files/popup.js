
chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
  const activeTab = tabs[0];
  const contentDiv = document.getElementById('content');

  if (activeTab.url && activeTab.url.startsWith('https://rishum.mta.ac.il/')) {
    // User is on the correct site, show the buttons
    contentDiv.innerHTML = `
      <h1>MTA Courses Planner</h1>
      <img src="mta-courses-planner-logo.jpg" alt="MTA Courses Planner Logo" id="logo">
      <button id="addPref">Enter Your Desired Courses</button>
      
    `;

    // Add event listeners for the buttons
    document.getElementById('addPref').addEventListener('click', function() {
      window.open('https://mta-courses-planner-f96c9.web.app', '_blank');
      alert('Add Preferences clicked');
    });

  } else {
    // User is not on the correct site, show the message
    contentDiv.innerHTML = '<p>' +
        '<b>MTA Courses Planner cannot access this page.</b>' +
        '<br>' +
        'You need to be on "Midanet" site' +'</p>';

  }
});

