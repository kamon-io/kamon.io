module Jekyll
  class Lightbox < Liquid::Block

    def initialize(tag_name, text, tokens)
      super

      @src = text.strip
    end

    def render(context)
      content = super.strip
      <<-HTML.gsub /^\s+/, '' # remove whitespaces from here
      <div class="w-100 my-4 text-center kamon-lightbox">
        <a class="no-decoration" href="#{@src}" data-caption="#{content}" data-fancybox="lightbox">
          <img class="img-fluid mb-2" src="#{@src}" alt="#{content}">
          <br>
          #{content}
        </a>
      </div>
      HTML
    end
  end
end

Liquid::Template.register_tag('lightbox', Jekyll::Lightbox)
