<html>
  <head>
    <title>${exportState.sourceBaseName} report</title>
    <style>
      img {
        width: ${width};
        heiht: ${height};
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
      <ul>
        <#list exportState.debug as item>
           <li>
             ${item.generation}
             <img src="${item.path}"/>
           </li>
        </#list>
      </ul>
    </div>
    <div>
      <h2>Result</h2>
      <img src="${exportState.resultRendering!''}"/>
    </div>
  </body>
</html>