#encoding: utf-8
module Jekyll
  class CodeExampleBlock < Liquid::Block

    def initialize(tag_name, text, tokens)
      super
    end

    def render(context)
      id_prefix = Random.rand(1000000).to_s
      serialised_snippets = super.split("\n---\n").drop(1)
      snippets = serialised_snippets.map { |snippet_string| YAML::load(snippet_string) }

      tab_header = '<div class="tab-header"><ul class="nav nav-pills language-tab" role="tablist">'
      tab_contents = '<div class="tab-content">'

      snippets.each_with_index { |snippet, index|
        active = ''

        if(index == 0)
          active = ' active'
        end

        snippet_id = snippet[:language] + '_' + index.to_s + '_' + id_prefix

        tab_header = tab_header +
          '<li class="nav-item">' +
            '<a class="nav-link btn btn-secondary btn-sm language-btn' + active + '" data-toggle="tab" data-target="#' + snippet_id + '" role="tab">' +
              snippet[:label] +
            '</a>' +
          '</li>'

        tab_contents = tab_contents +
          '<div class="tab-pane' + active + '" id="' + snippet_id + '" role="tabpanel">' +
            '<pre class="line-numbers language-'+ snippet[:language] +'">' +
              '<code class="language-'+ snippet[:language] +'">' +
                snippet[:code] +
              '</code>' +
            '</pre>' +
          '</div>'
      }

      tab_header = tab_header + '</ul></div>'
      tab_contents = tab_contents + '</div>'

      tab_footer = '<div class="tab-footer"><div class="fa fa-copy"/></div>'

      '<div class="code-example my-3 code-example-with-copy">' + tab_header + tab_contents + tab_footer + '</div>'
    end
  end

  class LanguageTag < Liquid::Tag

    # The format each language example is:
    #   {% language language_name path/to/file [start:n] [end:n] [label:"text"] %}
    #
    def initialize(tag_name, text, tokens)
      super
      parts = text.split(' ')
      if parts.count < 2
        puts "Can't initialise language tag without at least the language and file path parameters.".yellow
        return "Can't initialise language tag without at least the language and file path parameters".yellow
      end

      @language = parts[0]
      @file = parts[1]

      @tag = text.match(/\btag:([0-9A-Za-z\-\_]*)/)
      if @tag.nil?
        return "Not tag attribute specified"
      end

      @version = text.match(/\bversion:([0-9A-Za-z\-\_]*)/)
      @version = @version.nil? ? @version : @version.captures[0]

      @label = text.match(/\blabel:"(.*)"/)
      @label = @label.nil? ? @language.capitalize : @label.captures[0]

    end

    def render(context)
      code_dir = context.registers[:site].source
      version_dir = @version.nil? ? context.registers[:page]['path'].split(/\//)[1] : @version
      examples_dir = code_dir + "/docs/" + version_dir + "/examples"
      file_path = Pathname.new(examples_dir + "/" + @file).expand_path()

      unless file_path.file?
        puts "File #{file_path} could not be found. Requested from #{context.registers[:page]['path']}".yellow
        return "File #{file_path} could not be found".yellow
      end

      config = {}
      config[:language] = @language
      config[:code] = get_range(file_path.read, @tag)
      config[:label] = @label

      YAML::dump(config)
    end


    def get_range (code, tag)
      lines = code.lines.to_a
      line_count = lines.size
      start_line = code.lines.find_index { |l| l.include? "#{tag}:start" }
      end_line = code.lines.find_index { |l| l.include? "#{tag}:end" }

      raise "#{@file} does not contain the #{tag}:start element." if start_line.nil?
      raise "#{@file} does not contain the #{tag}:end element." if end_line.nil?

      code = lines.slice(start_line + 1, end_line - start_line - 1).join("")

      CGI::escapeHTML(code)
    end
  end
end

Liquid::Template.register_tag('language', Jekyll::LanguageTag)
Liquid::Template.register_tag('code_example', Jekyll::CodeExampleBlock)
