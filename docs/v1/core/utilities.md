---
title: 'Utilities | Kamon Documentation'
layout: docs
noindex: true
redirect_from:
  - /documentation/1.x/core/basics/utilities/
---

### Filters

Filters are used by several Kamon modules to determine whether to include or exclude certain application components from
metrics collection and tracing. All filters are configured under the `kamon.util.filters` configuration key:

{% code_example %}
{%   language typesafeconfig reference/core/src/main/resources/application.conf tag:filters label:"application.conf" %}
{% endcode_example %}

The rules for filter execution are simple: any string is accepted by the filter if it matches at least one of the
`includes` patterns and doesn't match any of the `excludes` patterns. By default all patterns are considered to be glob-like
patterns that accept literal string values to be matched and the following wildcards:
  - `*` match any number of characters up to the next '/' character found in the test string.
  - `?` match exactly one character, other than '/'.
  - `**` match any number of characters, regardless of any '/' character found after this wildcard.

Additionally, a matcher type prefix can be added to select a different type of matcher. The only supported prefixes are:
  - `glob:` specifies that the remaining of the string is a glob-like pattern.
  - `regex:` specifies that the remaining of the string is a regular expression pattern.

After filters have been defined they can be applied by using the `Kamon.filter(...)` function as shown below:


{% code_example %}
{%   language scala reference/core/src/main/scala/kamon/examples/scala/UtilitiesBasics.scala tag:applying-filters label:"Scala" %}
{% endcode_example %}