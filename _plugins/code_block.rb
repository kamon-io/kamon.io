module Jekyll
  class CodeBlockBlock < Liquid::Block

    def initialize(tag_name, text, tokens)
      super

      parts = text.split(' ')
      @language = parts[0].nil? ? "text" : parts[0]
      @file_name = parts[1].nil? ? "text" : parts[1]
    end

    def render(context)
      block_content = super.to_s.strip
      '<div class="code-example-container"><pre class="code-example language-'+ @language +'"><code class="language-'+ @language +'">'+ block_content +'</code></pre><div class="tab-footer"></div></div>'

    end
  end
end

Liquid::Template.register_tag('code_block', Jekyll::CodeBlockBlock)
