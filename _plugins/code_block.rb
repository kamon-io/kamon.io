module Jekyll
  class CodeBlockBlock < Liquid::Block

    def initialize(tag_name, text, tokens)
      super

      parts = text.split(' ')
      @language = parts[0].nil? ? "text" : parts[0]
    end

    def render(context)
      block_content = super.to_s.strip
      '<pre class="code-example line-numbers language-'+ @language +'"><code class="language-'+ @language +'">'+ block_content +'</code></pre>'

    end
  end
end

Liquid::Template.register_tag('code_block', Jekyll::CodeBlockBlock)
