Prism.languages.typesafeconfig = {
  'comment': /#.*$/m,
  'punctuation': /[{}:=\[\]]/g,
  'string': /("|')(\\?.)*?\1/gm
};

// Dummy definition to make prism work with plain text.
Prism.languages.text = {};
