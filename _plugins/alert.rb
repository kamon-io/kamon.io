module Jekyll
  class Alert < Liquid::Block

    def initialize(tag_name, text, tokens)
      super

      @type = text.strip
    end

    def render(context)
      content = super
      <<-HTML.gsub /^\s+/, '' # remove whitespaces from here
      <div class="outline-alert outline-alert-#{@type} row no-gutters">
        <div class="alert-image col-2 col-sm-1">
          <img src="/assets/img/icons/alert/#{@type}.svg"/>
        </div>
        <div class="alert-content col-10 col-sm-11">#{content}</div>
      </div>
      HTML
    end
  end
end

Liquid::Template.register_tag('alert', Jekyll::Alert)
