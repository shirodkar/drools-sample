document.addEventListener('DOMContentLoaded', function () {
    loadRulesInfo();

    document.getElementById('orderForm').addEventListener('submit', function (e) {
        e.preventDefault();
        evaluateOrder();
    });
});

function evaluateOrder() {
    var btn = document.getElementById('submitBtn');
    btn.disabled = true;
    btn.textContent = 'Evaluating...';

    var order = {
        totalAmount: parseFloat(document.getElementById('totalAmount').value),
        itemCount: parseInt(document.getElementById('itemCount').value),
        customerType: document.getElementById('customerType').value,
        couponCode: document.getElementById('couponCode').value.trim() || null
    };

    fetch('/api/orders/evaluate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(order)
    })
    .then(function (res) {
        if (!res.ok) throw new Error('Server error: ' + res.status);
        return res.json();
    })
    .then(function (result) {
        showResult(result);
    })
    .catch(function (err) {
        showError(err.message);
    })
    .finally(function () {
        btn.disabled = false;
        btn.textContent = 'Evaluate Rules';
    });
}

function showResult(r) {
    var card = document.getElementById('resultCard');
    card.style.display = 'block';
    card.className = 'card result-card show';

    document.getElementById('discountPct').textContent = r.discountPercentage + '%';
    document.getElementById('discountAmt').textContent = '$' + r.discountAmount.toFixed(2);
    document.getElementById('finalAmount').textContent = '$' + r.finalAmount.toFixed(2);

    var shippingEl = document.getElementById('shipping');
    shippingEl.textContent = r.freeShipping ? 'FREE' : 'Standard';
    shippingEl.className = 'stat-value ' + (r.freeShipping ? 'green' : 'red');

    document.getElementById('loyaltyPoints').textContent = r.loyaltyPoints;

    var rulesUl = document.getElementById('firedRules');
    rulesUl.innerHTML = '';
    r.firedRules.forEach(function (rule) {
        var li = document.createElement('li');
        li.textContent = rule;
        rulesUl.appendChild(li);
    });

    var msgsUl = document.getElementById('messages');
    msgsUl.innerHTML = '';
    r.messages.forEach(function (msg) {
        var li = document.createElement('li');
        li.textContent = msg;
        msgsUl.appendChild(li);
    });
}

function showError(msg) {
    var card = document.getElementById('resultCard');
    card.style.display = 'block';
    card.innerHTML = '<div class="error-msg">Error: ' + escapeHtml(msg) + '</div>';
}

function escapeHtml(text) {
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(text));
    return div.innerHTML;
}

function loadRulesInfo() {
    fetch('/api/rules/info')
        .then(function (res) { return res.json(); })
        .then(function (rules) {
            var container = document.getElementById('rulesInfo');
            if (rules.length === 0) {
                container.textContent = 'No rules loaded.';
                return;
            }
            var div = document.createElement('div');
            div.className = 'rule-list';
            rules.forEach(function (rule) {
                var chip = document.createElement('span');
                chip.className = 'rule-chip';
                chip.textContent = rule.name;
                div.appendChild(chip);
            });
            container.innerHTML = '';
            container.appendChild(div);
        })
        .catch(function () {
            document.getElementById('rulesInfo').textContent = 'Failed to load rules info.';
        });
}
