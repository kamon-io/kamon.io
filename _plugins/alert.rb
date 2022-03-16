module Jekyll
  class Alert < Liquid::Block

    def initialize(tag_name, text, tokens)
      super

      @type = text.strip
    end

    def render(context)
      content = super
      <<-HTML.gsub /^\s+/, '' # remove whitespaces from here
      <div class="outline-alert-#{@type} row no-gutters mt-3">
        <div class="alert-image col-auto pl-0 pr-3">
          <img height="32" width="32" src="/assets/img/icons/alert/#{@type}.svg"/>
        </div>
        <div class="alert-content col">#{content}</div>
      </div>
      HTML
    end
  end
end

Liquid::Template.register_tag('alert', Jekyll::Alert)
