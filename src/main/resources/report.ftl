<html>
  <head>
    <title>${exportState.sourceBaseName} report</title>
    <style>
      img {
        width: ${width};
        height: ${height};
      }
      .rendering {
        float: left;
      }
      .debug-control {
        clear: both;
      }
    </style>
  </head>
  <body>
    <div>
      <h2>Source</h2>
      <img src="${exportState.sourceRendering}"/>
    </div>
    <div>
      <h2>Debug</h2>
      <div class="debug-view">
        <#list exportState.renderings as rendering>
          <div class="rendering">
            <h3>${rendering}</h3>
            <img id="image-rendering-${rendering}">
          </div>
        </#list>
      </div>
      <div class="debug-control">
        <label for="gen">Generation:</label>
        <input name="gen" id="gen" type="range" value="0"
               min="0" max="${exportState.maxGeneration}"
               step="${exportState.generationStep}"
               onchange="updateDebugView(this.value)"/>
      </div>
      <script>
        var debugImages = [
          <#list exportState.debug as item>
            {src:"${item.path?js_string}", gen:${item.generation}, rendering:"${item.rendering}", norm:${item.norm}},
          </#list>
        ];
        function updateDebugView(gen) {
          <#list exportState.renderings as rendering>
            showRendering(gen, "${rendering}");
          </#list>
        }
        function showRendering(gen, rendering){
          for(const debugImage of debugImages){
            if(debugImage.gen == gen && debugImage.rendering == rendering) {
              document.getElementById('image-rendering-' + rendering).src = debugImage.src;
              return;
            }
          }
        }
      </script>
    </div>
    <div>
      <h2>Result</h2>
      <img src="${exportState.resultRendering!''}"/>
    </div>
  </body>
</html>