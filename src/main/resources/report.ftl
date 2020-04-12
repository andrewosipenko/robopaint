<html>
  <head>
    <title>${exportState.sourceBaseName} report</title>
    <style>
      figure {
        width: 450px;
        height: 450px;
        margin: 0;
        overflow: hidden;
      }
      img {
        width: ${width}px;
        height: ${height}px;
        ${transform};
      }
      .source, .result, .rendering {
        float: left;
      }
      .debug, debug-view {
        clear: both;
      }
    </style>
  </head>
  <body>
    <div class="source">
      <h2>Source</h2>
      <figure>
        <img src="${exportState.sourceRendering}"/>
      </figure>
    </div>
    <div class="result">
      <h2>Result</h2>
      <figure>
        <img src="${exportState.resultRendering!''}"/>
      </figure>
    </div>
    <div class="debug">
      <h2>Debug</h2>
      <div class="debug-view">
        <#list exportState.renderings as rendering>
          <div class="rendering">
            <h3>${rendering}</h3>
            <figure>
              <img id="image-rendering-${rendering}">
            </figure>
          </div>
        </#list>
      </div>
      <div style="clear:both"/>
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
        updateDebugView(document.getElementById('gen').value);
      </script>
    </div>
  </body>
</html>