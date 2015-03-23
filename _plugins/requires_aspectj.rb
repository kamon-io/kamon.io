module Jekyll
  class RequiresAspectJTag < Liquid::Tag

    def initialize(tag_name, text, tokens)
      super
    end

    def render(context)
      "<small><span class=\"label label-info\">requires aspectj</span></small>"
    end
  end
end

Liquid::Template.register_tag('requires_aspectj', Jekyll::RequiresAspectJTag)
