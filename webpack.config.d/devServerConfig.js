 config.devServer = {
    ...config.devServer, // Preserve existing settings
    historyApiFallback: true,
    port: 9000, // Change the port
    // Add other custom devServer options
  };