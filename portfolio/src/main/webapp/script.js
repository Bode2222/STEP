// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*
* Gets the title, descriptions and english captions(if specified video has english captions) from youtube api
* using video id specified via user.
*/
function getVideoData(){
    var videoId = document.getElementById("VideoIdTextBox").value;
    var metadataServlet = "VideoMetadata?videoId=" + videoId;
    var captionsServlet = "YoutubeCaptions?videoId=" + videoId;
    fetch(metadataServlet).then(response => response.json()).then((video) => {
        document.getElementById("videoTitle").innerHTML = video.items[0].snippet.title;
        document.getElementById("videoDescription").innerHTML = video.items[0].snippet.localized.description;
    });
    fetch(captionsServlet).then(response =>response.text()).then(captions => {
        document.getElementById("videoCaptions").innerHTML = captions;
    })
}
