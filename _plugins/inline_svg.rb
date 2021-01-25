# frozen_string_literal: true

require "jekyll"
require "nokogiri"

module Jekyll
  class InlineSVG

    def self.process(content)
      html = content.output
      content.output = inline_svgs(html, content, content.site.source) if contains_svg?(html)
    end

    def self.should_process(doc)
      (doc.is_a?(Jekyll::Page) || doc.write?) && doc.output_ext == ".html"
    end

    def self.contains_svg?(html)
      html.match?(/src=".*\.svg"/) != nil
    end

    def self.inline_svgs(html, content, source_dir)
      content = Nokogiri.HTML(html)
      content.css("img").each do |img|
        svg_path = Pathname.new(source_dir + img.attr("src"))

        if img.attr("src").end_with?(".svg") && svg_path.file? && svg_path.size < (5 * 1024)
          svg_node = content.create_element "div"
          svg_node.set_attribute("class", img.attr("class"))
          svg_node.inner_html = svg_path.read
          img.replace svg_node
        end
      end

      content.to_html
    end
  end
end

Jekyll::Hooks.register [:pages, :documents], :post_render do |doc|
  # Jekyll::InlineSVG.process(doc) if Jekyll::InlineSVG.should_process(doc)
end