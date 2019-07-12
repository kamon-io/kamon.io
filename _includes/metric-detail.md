{% assign path_sections = page.url | split: '/' %}
{% assign current_version = path_sections[2] %}
{% assign metric_info = site.data.docs[current_version].metrics[include.name] %}

<div class="row metric-detail py-2 my-2" markdown="0">
  <div class="col-12 w-100">
  <a data-toggle="collapse" data-target="#{{ include.name | replace: ".", "_" }}">
    <strong class="mr-2 w-100">{{ include.name}}</strong> {{ metric_info.instrument_type }}
    </a>
  </div>
  <div id="{{ include.name | replace: ".", "_" }}" class="col-12 collapse">
    <div class="mt-2">{{ metric_info.description }}</div>
    {% if metric_info.tags %}
      <br>
      <p>Instruments are tagged with:
        <ul>
          {% for tag in metric_info.tags %}
            <li><span class="tag-name">{{ tag.name }}</span>: {{ tag.description }}</li>
          {% endfor %}
        </ul>
      </p>
    {% endif %}
  </div>
</div>