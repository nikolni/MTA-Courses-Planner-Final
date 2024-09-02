chrome.runtime.onInstalled.addListener(() => {
  console.log("Extension installed");
});

// chrome.action.onClicked.addListener((tab) => {
//   if (tab.url.includes("https://www.mta.ac.il/info_net")) {
//     chrome.scripting.executeScript({
//       target: { tabId: tab.id }
//     });
//   }
// });
